/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mysql.cj.MysqlType;
import dto.ResponseDto;
import entity.size;
import entity.Brand;
import entity.Product1;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Dell
 */
@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            String productTd = request.getParameter("id");            
            
            Gson gson = new Gson();
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            ResponseDto response_DTO = new ResponseDto();
            
            if(Validation.isNotInteger(productTd)){
                response_DTO.setContent("Product not found");
            }else{
                Product1 product = (Product1) session.get(Product1.class,Integer.valueOf(productTd));
                product.getUser().setEmail(null);
                product.getUser().setPassword(null);
                product.getUser().setVerification(null);
                
                
                Criteria criteria1 = session.createCriteria(Brand.class);
                criteria1.add(Restrictions.eq("category",product.getBrand().getCategory()));
                List<Brand> brandList = criteria1.list();
                
                Criteria criteria2 = session.createCriteria(Product1.class);
                criteria2.add(Restrictions.in("brand", brandList));
                criteria2.add(Restrictions.ne("id", product.getId()));
                criteria2.setMaxResults(4);
                
                List<Product1> productList = criteria2.list();
                
                for (Product1 product1 : productList) {
                    product1.getUser().setEmail(null);
                    product1.getUser().setPassword(null);
                    product1.getUser().setVerification(null);
                }
                
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("product",gson.toJsonTree(product));
                jsonObject.add("productList",gson.toJsonTree(productList));
                
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(jsonObject));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

}
