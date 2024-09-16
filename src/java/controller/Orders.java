/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.ResponseDto;
import dto.UserDto;
import entity.Brand;
import entity.OrderItem;
import entity.Product1;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "Orders", urlPatterns = {"/Orders"})
public class Orders extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        HttpSession session=req.getSession();
        Session session1 = HibernateUtil.getSessionFactory().openSession();
        ResponseDto response_dto=new ResponseDto();
        Gson gson=new Gson();
        
        if(session.getAttribute("user")!=null){
            UserDto userDto = (UserDto) req.getSession().getAttribute("user");
                
              
            
            Criteria criteria = session1.createCriteria(User.class);
            criteria.add(Restrictions.eq("email",userDto.getEmail()));
            User user = (User) criteria.uniqueResult();
            
            Criteria criteria1 = session1.createCriteria(Orders.class);
//                criteria1.add(Restrictions.eq("user",user));
                List<Orders> orderList = criteria1.list();
                System.out.println(orderList);
//                 Criteria criteria2 = session1.createCriteria(OrderItem.class);
//                criteria2.add(Restrictions.eq("order",orderList));
//                List<OrderItem> orderitemList = criteria2.list();
                
                JsonObject jsonObject = new JsonObject();
//                jsonObject.add("orderitemList",gson.toJsonTree(orderitemList));
                jsonObject.add("orderList",gson.toJsonTree(orderList));
                
                resp.setContentType("application/json");
                resp.getWriter().write(gson.toJson(orderList));
        }
        
    }

    
}
