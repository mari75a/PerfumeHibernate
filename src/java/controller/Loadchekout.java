/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.UserDto;
import entity.Address;
import entity.Cart;
import entity.City;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import entity.User;
import java.util.List;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Dell
 */
@WebServlet(name = "Loadchekout", urlPatterns = {"/Loadchekout"})
public class Loadchekout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", Boolean.FALSE);
        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
        if(httpSession.getAttribute("user")!=null){
            UserDto userDto = (UserDto) httpSession.getAttribute("user");
            
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email",userDto.getEmail()));
            User user = (User) criteria.uniqueResult();
            
            Criteria criteria1 = session.createCriteria(Address.class);
            criteria1.add(Restrictions.eq("user", user));
            criteria1.addOrder(Order.desc("id"));
            
//            Address address = (Address) criteria1.list().get(0);
            
            Criteria criteria2 = session.createCriteria(Cart.class);
            criteria2.add(Restrictions.eq("user", user));
            criteria2.addOrder(Order.asc("id"));
            List<Cart> cartLIst = criteria2.list();
            
            Criteria criteria3 = session.createCriteria(City.class);
            criteria3.addOrder(Order.asc("name"));
            List<City> cityList = criteria3.list();
            
            jsonObject.add("cityList", gson.toJsonTree(cityList));
            
//            address.setUser(null);
//            jsonObject.add("address",gson.toJsonTree(address));
            
            for (Cart cart : cartLIst) {
                cart.setUser(null);
                cart.getProduct().setUser(null);
            }
            
            jsonObject.add("cartList",gson.toJsonTree(cartLIst));
            
            jsonObject.addProperty("success", true);
        }else{
            jsonObject.addProperty("message", "User nor signed in");
        }
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
        session.close();
    }

}
