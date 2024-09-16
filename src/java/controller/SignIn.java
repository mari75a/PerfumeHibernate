package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Cart_DTO;
import dto.ResponseDto;
import dto.UserDto;
import entity.User;
import entity.Cart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        UserDto user_DTO = gson.fromJson(request.getReader(), UserDto.class);

        ResponseDto response_DTO = new ResponseDto();

        if (user_DTO.getEmail().isEmpty()) {
            response_DTO.setContent("Please enter your Email");
        } else if (user_DTO.getPassword().isEmpty()) {
            response_DTO.setContent("Please Enter your Password");
        } else {

            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            criteria1.add(Restrictions.eq("password", user_DTO.getPassword()));

            if (!criteria1.list().isEmpty()) {

                User user = (User) criteria1.list().get(0);

                if (!user.getVerification().equals("Verified")) {

                    request.getSession().setAttribute("email", user_DTO.getEmail());
                    response_DTO.setContent("Unverified");

                } else {
                    user_DTO.setFirstname(user.getFirstName());
                    user_DTO.setLastname(user.getLastName());
                    user_DTO.setEmail(user.getEmail());
                    user_DTO.setPassword(null);
                    request.getSession().setAttribute("user", user_DTO);

                    //Move Session Cart to DB Cart
                    if (request.getSession().getAttribute("sessionCart") != null) {
                        ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) request.getSession().getAttribute("sessionCart");

                        Criteria criteria2 = session.createCriteria(Cart.class);
                        criteria2.add(Restrictions.eq("user", user));
                        List<Cart> dbCart = criteria2.list();

                        if (dbCart.isEmpty()) {
                            //Database Cart is Empty
                            //Add all  session cart items into DB Cart
                            for (Cart_DTO cart_DTO : sessionCart) {
                                Cart cart = new Cart();
                                cart.setProduct(cart_DTO.getProduct());
                                cart.setQty(cart_DTO.getQty());
                                cart.setUser(user);
                                session.save(cart);
                                response_DTO.setSuccess(true);
                                response_DTO.setContent("Session Cart Items added to DB Cart");
                            }
                        } else {
                            //Found items in DB Cart
                            for (Cart_DTO cart_DTO : sessionCart) {

                                boolean isFound_inDBCart = false;
                                for (Cart cartObj : dbCart) {

                                    if (cart_DTO.getProduct().getId() == cartObj.getProduct().getId()) {
                                        //Same items found in session cart & Database Cart 
                                        isFound_inDBCart = true;

                                        if ((cart_DTO.getQty() + cartObj.getQty()) <= cartObj.getProduct().getQty()) {
                                            //QTY Avaliable
                                            cartObj.setQty(cart_DTO.getQty() + cartObj.getQty());
                                            session.update(cartObj);
                                            response_DTO.setSuccess(true);
                                            response_DTO.setContent("Cart Item Updated Successfully");

                                        } else {
                                            //QTY not Avaliable
                                            cartObj.setQty(cartObj.getProduct().getQty());
                                            session.update(cartObj);
                                            response_DTO.setSuccess(true);
                                            response_DTO.setContent("Cart Item Added Successfully");
                                        }
                                    }
                                }
                                if (!isFound_inDBCart) {
                                    //Product not found in DB Cart
                                    Cart cart = new Cart();
                                    cart.setProduct(cart_DTO.getProduct());
                                    cart.setQty(cart_DTO.getQty());
                                    cart.setUser(user);
                                    session.save(cart);
                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Cart Item Added to Session Cart");
                                }
                            }
                        }
                        request.getSession().removeAttribute("sessionCart");
                        session.beginTransaction().commit();
                    } else {
                        response_DTO.setContent("Nothing to be transfered Session Cart to Database Cart");
                    }
                    response_DTO.setSuccess(true);
                    response_DTO.setContent("Sign in success");
                }

            } else {
                response_DTO.setContent("Invalid Details! Please try again !!!");
            }

        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
