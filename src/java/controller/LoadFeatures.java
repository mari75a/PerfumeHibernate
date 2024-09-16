/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.ResponseDto;
import entity.Brand;
import entity.Category;


import entity.size;
import entity.product_status;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author Dell D
 */
@WebServlet(name = "LoadFeatures", urlPatterns = {"/LoadFeatures"})
public class LoadFeatures extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResponseDto response_DTO = new ResponseDto();
        Gson gson = new Gson();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Criteria criteria1 = session.createCriteria(Category.class);
        criteria1.addOrder(Order.asc("name"));        
        List<Category> categoryList = criteria1.list();
        
        Criteria criteria2 = session.createCriteria(Brand.class);
        criteria2.addOrder(Order.asc("name"));        
        List<Brand> brandList = criteria2.list();
//        
        Criteria criteria3 = session.createCriteria(size.class);
                
        List<size> sizeList = criteria3.list();
//        
//        Criteria criteria4 = session.createCriteria(ProductCondition.class);
//        criteria4.addOrder(Order.asc("name"));        
//        List<ProductCondition> conditionList = criteria4.list();
//        
//        Criteria criteria5 = session.createCriteria(Storage.class);
//        criteria5.addOrder(Order.asc("id"));        
//        List<Storage> storageList = criteria5.list();
        
        JsonObject jsonObject = new JsonObject();
        
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        jsonObject.add("brandList", gson.toJsonTree(brandList));
        jsonObject.add("sizeList", gson.toJsonTree(sizeList));
//        jsonObject.add("conditionList", gson.toJsonTree(conditionList));
//        jsonObject.add("storageList", gson.toJsonTree(storageList));
        
        session.close();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
    }

    
}
