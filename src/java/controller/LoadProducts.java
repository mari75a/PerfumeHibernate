/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.mysql.cj.Session;
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
import org.hibernate.Query;
import org.hibernate.Transaction;

/**
 *
 * @author sange
 */
@WebServlet(name = "LoadProducts", urlPatterns = {"/LoadProducts"})
public class LoadProducts extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      
        
        
        Gson gson = new Gson();
        org.hibernate.Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        Query q=session.createQuery("from Product1");
        List<Product1> products=q.list();
       
        tx.commit();
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(products));
       
       
        
        
       
        
    }

    
    
}
