
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.size;
import entity.Product1;
import entity.product_status;
import entity.Brand;
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


@WebServlet(name = "LoadSearchData", urlPatterns = {"/LoadSearchData"})
public class LoadSearchData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        //main code
        //get category list from db        
        jsonObject.add("categoryList", gson.toJsonTree(session.createCriteria(Category.class).list()));
        
        //get condition list
        
        
        //get colors
        jsonObject.add("sizeList", gson.toJsonTree(session.createCriteria(size.class).list()));
        
        //get storage
        jsonObject.add("brandList", gson.toJsonTree(session.createCriteria(Brand.class).list()));
        
        //get products
        Criteria criteria = session.createCriteria(Product1.class);
        jsonObject.addProperty("totalProductsCount", criteria.list().size());
        criteria.setFirstResult(1);
        criteria.setMaxResults(4);
        
        
        List<Product1> productList = criteria.list();
        for (Product1 product : productList) {
            product.setUser(null);
        }
        jsonObject.add("productList", gson.toJsonTree(productList));
        jsonObject.addProperty("success", true);
        //main code
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
    }

  

}
