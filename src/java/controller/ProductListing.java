/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.ResponseDto;
import dto.UserDto;
import entity.Brand;
import entity.Category;

import entity.size;
import entity.Product1;

import entity.User;
import entity.product_status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "ProductListing", urlPatterns = {"/ProductListing"})
public class ProductListing extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();

        String categoryId = request.getParameter("categoryId");
        String brandId = request.getParameter("brandId");
        String sizeId = request.getParameter("sizeId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String price = request.getParameter("price");
        String quantity = request.getParameter("quantity");

        Part part1 = request.getPart("img1");

        ResponseDto response_DTO = new ResponseDto();
        if (Validation.isNotInteger(categoryId)) {
            response_DTO.setContent("Invalid category");
      }         else if (Validation.isNotInteger(brandId)) {
           response_DTO.setContent("Invalid brand");
       } 
        else if (Validation.isNotInteger(sizeId)) {
            response_DTO.setContent("Invalid size");
        } else if (title.isEmpty()) {
            response_DTO.setContent("Please fill product title");
        } else if (description.isEmpty()) {
            response_DTO.setContent("Please fill product description");
        } else if (price.isEmpty()) {
            response_DTO.setContent("Please fill product price");
        } else if (Validation.isPriceInvalid(price)) {
            response_DTO.setContent("Invalid price");
        } else if (Validation.isNotInteger(quantity)) {
            response_DTO.setContent("Invalid quantity");
        } else if (Integer.parseInt(quantity) <= 0) {
            response_DTO.setContent("Quantity must be grator than 0");
        } else if (part1.getSubmittedFileName() == null) {
            response_DTO.setContent("please Upload three images  1");
        } else {
            Category category = (Category) session.load(Category.class,
                    Integer.parseInt(categoryId));

            if (category == null) {
                response_DTO.setContent("Please select a valid category");
            } else {

                product_status status = (product_status) session.load(product_status.class, 1);

                UserDto dto = (UserDto) request.getSession().getAttribute("user");
                Criteria criteria = session.createCriteria(User.class);
                criteria.add(Restrictions.eq("email", dto.getEmail()));
                User user = (User) criteria.uniqueResult();

                Criteria criteria1 = session.createCriteria(size.class);
                criteria1.add(Restrictions.eq("id", Integer.parseInt(sizeId)));
                size size1 = (size) criteria1.uniqueResult();
                
                Criteria criteria2 = session.createCriteria(Brand.class);
                criteria2.add(Restrictions.eq("id", Integer.parseInt(brandId)));
                Brand brand = (Brand) criteria2.uniqueResult();
                
                Product1 product = new Product1();

                product.setDatetime(new Date());
                product.setDescription(description);
                product.setSize(size1);
                product.setPrice(Double.parseDouble(price));
                product.setQty(Integer.parseInt(quantity));
                product.setStatus(status);
                product.setBrand(brand);
                product.setTitle(title);
                product.setUser(user);

                int pid = (int) session.save(product);
                System.out.println(pid);

                if (true) {
                    String realPath = request.getServletContext().getRealPath("").replace("build" + File.separator + "web", "web");
                    System.out.println(realPath);
                    System.out.println(request.getServletContext().getRealPath(""));
                    File productDir = new File(realPath + File.separator + "product");
                    if (!productDir.exists()) {
                        productDir.mkdir();
                    }

                    File pDir = new File(productDir, File.separator + pid);
                    if (!pDir.exists()) {
                        pDir.mkdir();
                    }

                    InputStream input1 = part1.getInputStream();

                    File file1 = new File(pDir, "image1.png");

                    Files.copy(input1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                }

                session.beginTransaction().commit();

                response_DTO.setSuccess(true);
                response_DTO.setContent("New product added");
            }
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));

        session.close();
    }
}


    
