/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.mysql.cj.Session;
import dto.ResponseDto;
import dto.UserDto;
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
import model.HibernateUtil;
import org.apache.jasper.tagplugins.jstl.ForEach;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sange
 */
@WebServlet(name = "LoadUserProduct", urlPatterns = {"/LoadUserProduct"})
public class LoadUserProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        ResponseDto responseDto = new ResponseDto();
        Gson gson = new Gson();
        if (req.getSession().getAttribute("user") != null) {
            //db cart
            UserDto userDto = (UserDto) req.getSession().getAttribute("user");

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", userDto.getEmail()));
            User user = (User) criteria1.uniqueResult();
            
            Criteria criteria =session.createCriteria(Product1.class);
            criteria.add(Restrictions.eq("user", user));
            List<Product1> productlist=criteria.list();

            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(productlist));

        }else{
            System.out.println("Error no user");
        }
        
        
        
       
       
       
        
        
       
        
    }

    
    
}
