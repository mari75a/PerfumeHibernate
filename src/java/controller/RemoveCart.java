/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.mysql.cj.MysqlType;
import dto.Cart_DTO;
import dto.ResponseDto;
import dto.UserDto;
import entity.Cart;
import entity.Product1;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Dell
 */
@WebServlet(name = "RemoveCart", urlPatterns = {"/RemoveCart"})
public class RemoveCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        ResponseDto responseDto = new ResponseDto();

        try {

            String id = request.getParameter("id");
           
             
             if (Validation.isNotInteger(id)) {
                //invalid product id
                responseDto.setContent("Product not found");
            } else {
                Product1 product = (Product1) session.get(Product1.class, Integer.valueOf(id));
                if (product != null) {
                    //product found
                    if (request.getSession().getAttribute("user") != null) {
                        //db cart
                        //check db cart for already added items
                        //get userdto from session
                        UserDto userDto = (UserDto) request.getSession().getAttribute("user");

                        //search user
                        Criteria criteria1 = session.createCriteria(User.class);
                        criteria1.add(Restrictions.eq("email", userDto.getEmail()));
                        User user = (User) criteria1.uniqueResult();

                        Criteria criteria2 = session.createCriteria(Cart.class);
                        criteria2.add(Restrictions.eq("product", product));
                        criteria2.add(Restrictions.eq("user", user));

                        if (criteria2.list().isEmpty()) {
                            //item not found in the cart

                            if (true) {
                                //add product to cart
                                Cart cart = new Cart();
//                                cart.setQty(Integer.valueOf(qty));
                                cart.setProduct(product);
                                cart.setUser(user);
                                session.save(cart);
                                transaction.commit();
                                responseDto.setContent("Product Added to cart successfully");
                                responseDto.setSuccess(true);
                            } else {
                                //insufficient quantity
                                responseDto.setContent("Insufficient quantity");
                            }
                        } else {
                            //item already found in the cart
                            Cart cart = (Cart) criteria2.uniqueResult();

                          
                                session.delete(cart);
                                transaction.commit();
                                responseDto.setContent("Product Removed from cart successfully");
                                responseDto.setSuccess(true);
                           
                        }

                    } else {
                        //session cart

                        if (request.getSession().getAttribute("sessionCart") != null) {
                            //session cart found
                            ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) request.getSession().getAttribute("sessionCart");

                            Cart_DTO foundCartDto = null;

                            for (Cart_DTO cartDto : sessionCart) {
                                if (cartDto.getProduct().getId() == product.getId()) {
                                    foundCartDto = cartDto;
                                    break;
                                }
                            }
                            if (foundCartDto != null) {
                                //product found
                                if (foundCartDto.getQty() + 1 <= product.getQty()) {
                                    foundCartDto.setQty(foundCartDto.getQty() + 1);
                                    responseDto.setContent("Product Added to cart successfully");
                                    responseDto.setSuccess(true);
                                } else {
                                    //insufficient quantity
                                    responseDto.setContent("Insufficient quantity");
                                }
                            } else {
                                //product not found
                                if (1 <= product.getQty()) {
                                    Cart_DTO cartDto = new Cart_DTO();
                                    cartDto.setQty(1);
                                    cartDto.setProduct(product);
                                    sessionCart.add(cartDto);
                                    responseDto.setContent("Product Added to cart successfully");
                                    responseDto.setSuccess(true);
                                } else {
                                    //insufficient quantity
                                    responseDto.setContent("Insufficient quantity");
                                }
                            }
                        } else {
                            //session cart not found
                            if (1 <= product.getQty()) {
                                //add to session cart
                                ArrayList<Cart_DTO> sessionCart = new ArrayList();

                                Cart_DTO cartDto = new Cart_DTO();
                                cartDto.setProduct(product);
                                cartDto.setQty(1);

                                sessionCart.add(cartDto);
                                request.getSession().setAttribute("sessionCart", sessionCart);
                                responseDto.setContent("Product Added to cart successfully");
                                responseDto.setSuccess(true);
                            } else {
                                //insufficient quantity
                                responseDto.setContent("Insufficient quantity");
                            }
                        }

                    }
                } else {
                    responseDto.setContent("Product not found");
                }
            }

        } catch (Exception e) {
            responseDto.setContent("Unable to process your request pleasse try again later");
            transaction.rollback();
            e.printStackTrace();
        }finally{
            session.close();
        }
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(responseDto));
        
    }

}
