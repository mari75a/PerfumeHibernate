package controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Category;
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
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "SearchProducts", urlPatterns = {"/SearchProducts"})
public class SearchProducts extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject responseJsonObject = new JsonObject();
        Gson gson = new Gson();
        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        Session session = HibernateUtil.getSessionFactory().openSession();

        //search all products from db
        Criteria criteria = session.createCriteria(Product1.class);
        if(requestJsonObject.has("search")){
         String search = requestJsonObject.get("search").getAsString();
         criteria.add(Restrictions.ilike("title", "%" +search+"%") );
        }
        
        if (requestJsonObject.has("category")) {
            //categoru selected
            String category_id = requestJsonObject.get("category").getAsString();
            System.out.println(category_id);

            //get category list from db
            Criteria criteria1 = session.createCriteria(Category.class);
            criteria1.add(Restrictions.eq("id", Integer.parseInt(category_id)));

            if (!criteria1.list().isEmpty()) {
                List<Category> categoryList = criteria1.list();

                //filter model by category list from db
                Criteria criteria2 = session.createCriteria(Brand.class);
                criteria2.add(Restrictions.in("category", categoryList));
                List<Brand> brandList = criteria2.list();

                //filter products by model
                criteria.add(Restrictions.in("brand", brandList));
            }

        }
        if (requestJsonObject.has("brand")) {
            //condition selected
            String brand_id = requestJsonObject.get("brand").getAsString();
            System.out.println(brand_id);
            //get condition from db
            Criteria criteria3 = session.createCriteria(Brand.class);
            criteria3.add(Restrictions.eq("id", Integer.parseInt(brand_id)));
            Brand brand = (Brand) criteria3.uniqueResult();

            //filter products by condition
            criteria.add(Restrictions.eq("brand", brand));
        }
        if (requestJsonObject.has("sort_text")) {
            String sort_text = requestJsonObject.get("sort_text").getAsString();
            System.out.println(sort_text);
            switch (sort_text) {
                case "Short by Latest":
                    criteria.addOrder(Order.desc("id"));
                    break;
                case "Short by Oldest":
                    criteria.addOrder(Order.asc("id"));
                    break;
                case "Short by Name":
                    criteria.addOrder(Order.asc("title"));
                    break;
                case "Short by Price":
                    criteria.addOrder(Order.asc("price"));
                    break;
                default:
                    break;
            }
        }
        if (requestJsonObject.has("min_price") && requestJsonObject.has("max_price")) {
            double max = requestJsonObject.get("max_price").getAsDouble();
            double min = requestJsonObject.get("min_price").getAsDouble();
            System.out.println(max);
            System.out.println(min);
            criteria.add(Restrictions.ge("price", min));
            criteria.add(Restrictions.le("price", max));
        }

        
        List<Product1> products = criteria.list();
        //get all product count
        responseJsonObject.addProperty("totalProductsCount", products.size());
        responseJsonObject.addProperty("success", true);
        int firstResult = requestJsonObject.get("firstResult").getAsInt();
        criteria.setFirstResult(firstResult);
        criteria.setMaxResults(4);
        responseJsonObject.add("productList", gson.toJsonTree(criteria.list()));

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
        session.close();
    }

}
