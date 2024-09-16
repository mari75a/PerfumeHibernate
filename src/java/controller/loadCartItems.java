/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.ResponseDto;
import dto.UserDto;
import entity.Cart;
import entity.Product1;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Dell
 */
@WebServlet(name = "loadCartItems", urlPatterns = {"/loadCartItems"})
public class loadCartItems extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        ResponseDto responseDto = new ResponseDto();
        ArrayList<Cart_DTO> cartDtoList = new ArrayList<>();
        try {
            if(request.getSession().getAttribute("user")!=null){
                //db cart
                UserDto userDto = (UserDto) request.getSession().getAttribute("user");
                
                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email",userDto.getEmail()));
                User user = (User) criteria1.uniqueResult();
                
                Criteria criteria2 = session.createCriteria(Cart.class);
                criteria2.add(Restrictions.eq("user", user));
                
                List<Cart> cartList = criteria2.list();
                
                for (Cart cart : cartList) {
                    Cart_DTO cartDto = new Cart_DTO();
                    
                    Product1 product = cart.getProduct();
                    product.setUser(null);
                    
                    cartDto.setProduct(product);
                    cartDto.setQty(cart.getQty());
                    
                    cartDtoList.add(cartDto);
                }
            }else{
                //session cart
                if(request.getSession().getAttribute("sessionCart")!=null){
                    cartDtoList = (ArrayList<Cart_DTO>) request.getSession().getAttribute("sessionCart");
                    
                    for (Cart_DTO cartDto : cartDtoList) {
                        cartDto.getProduct().setUser(null);
                    }
                }else{
                    //empty session cart
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(cartDtoList));
    }


}
