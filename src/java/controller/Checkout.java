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
import entity.OrderItem;
import entity.Order_Status;
import entity.Product1;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity.User;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import model.HibernateUtil;
import model.PayHere;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Dell
 */
@WebServlet(name = "Checkout", urlPatterns = {"/Checkout"})
public class Checkout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);
        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        try {
            String fname = jsonObject.get("fname").getAsString();
            String lname = jsonObject.get("lname").getAsString();
            String cityId = jsonObject.get("city").getAsString();
            String line1 = jsonObject.get("line1").getAsString();
            String line2 = jsonObject.get("line2").getAsString();
            String postalcode = jsonObject.get("postalcode").getAsString();
            String mobile = jsonObject.get("mobile").getAsString();
            boolean isCurrentAddress = jsonObject.get("isCurrentAddress").getAsBoolean();

            if (request.getSession().getAttribute("user") != null) {
                UserDto userDto = (UserDto) request.getSession().getAttribute("user");
                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email", userDto.getEmail()));
                User user = (User) criteria1.uniqueResult();

                if (isCurrentAddress) {
                    //check and get current address
                    Criteria criteria2 = session.createCriteria(Address.class);
                    criteria2.add(Restrictions.eq("user", user));
                    criteria2.addOrder(Order.desc("id"));
                    criteria2.setMaxResults(1);

                    if (criteria2.list().isEmpty()) {
                        //not any address found
                        responseJsonObject.addProperty("error", "No address saved please save an address first");
                    } else {
                        //get current address
                        Address address = (Address) criteria2.list().get(0);
                        saveOrders(address, user, session, transaction, responseJsonObject);
                    }
                } else {
                    //new address
                    if (fname.isEmpty()) {

                    } else if (lname.isEmpty()) {

                    } else if (Validation.isNotInteger(cityId)) {

                    } else {
                        Criteria criteria3 = session.createCriteria(City.class);
                        criteria3.add(Restrictions.eq("id", Integer.parseInt(cityId)));
                        if (criteria3.list().isEmpty()) {
                            responseJsonObject.addProperty("error", "Invalid city");
                        } else {
                            //city found
                            City city1 = (City) criteria3.list().get(0);

                            if (line1.isEmpty()) {
                                responseJsonObject.addProperty("error", "Please fill line1");
                            } else if (line2.isEmpty()) {
                                responseJsonObject.addProperty("error", "Please fill line2");
                            } else if (postalcode.isEmpty()) {
                                responseJsonObject.addProperty("error", "please fill postal code");
                            } else if (postalcode.length() != 5) {
                                responseJsonObject.addProperty("error", "Invalid postal code");
                            } else if (Validation.isNotInteger(postalcode)) {
                                responseJsonObject.addProperty("error", "Invalid invalid postal code");
                            } else if (mobile.isEmpty()) {
                                responseJsonObject.addProperty("error", "Please enter mobile");
                            } else if (Validation.isMobileInvalid(mobile)) {
                                responseJsonObject.addProperty("error", "Invalid mobile");
                            } else {
                                //save new address
                                Address address = new Address();
                                address.setCity(city1);
                                address.setFirstname(fname);
                                address.setLastname(lname);
                                address.setLine1(line1);
                                address.setLine2(line2);
                                address.setMobile(mobile);
                                address.setPostal_code(Integer.parseInt(postalcode));
                                address.setUser(user);

                                session.save(address);
                                transaction.commit();
                                responseJsonObject.addProperty("message", "Success");
                                responseJsonObject.addProperty("success", true);
                                
                               saveOrders(address, user, session, transaction, responseJsonObject);
                            }
                        }
                    }
                }
            } else {
                //user not signed in
                responseJsonObject.addProperty("error", "User not signed in");
            }
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            responseJsonObject.addProperty("error", "Something wend wrong: Exception");
        } finally {
            session.close();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
    }

    private void saveOrders(Address address, User user, Session session, Transaction transaction, JsonObject responseJsonObject) throws Exception {
        entity.Orders order = new entity.Orders();
        order.setAddress(address);
        order.setDate_time(new Date());
        order.setUser(user);

       
        
        int order_id = (int) session.save(order);
        Transaction trx=session.beginTransaction();
        
        
        Criteria criteria4 = session.createCriteria(Cart.class);
       criteria4.add(Restrictions.eq("user", user));
        List<Cart> cartList = criteria4.list();
Order_Status order_Status = (Order_Status) session.get(Order_Status.class, 1);
         

           
        
        double total = 0;
        String items= "";
        for (Cart item : cartList) {
            double ship = 2500;
            if(order.getAddress().getCity().getId()==1){
                ship=1000;
            }
           total += (item.getQty()*item.getProduct().getPrice())+(item.getQty()*ship);
           items.concat(item.getProduct().getTitle()+", ");
            Product1 product = item.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setOrder_Status(order_Status);
            orderItem.setProduct(item.getProduct());
            orderItem.setQty(item.getQty());
 
            session.save(orderItem);
            
            product.setQty(product.getQty() - item.getQty());
           session.update(product);

            
            session.delete(item);
        }
        
         trx.commit();
        
        String merchant_id = "1228213";
//        
        String format = new DecimalFormat("0.00").format(total);
//                        
       String merchantSecretmd5Hash=PayHere.getMd5("MTc0MTgwNjU3NzMzMjk0ODA5MTE1NzEyMjUxMzc4OTIwMjgyMTA=");
//        //set payment data
        JsonObject payment = new JsonObject();
        payment.addProperty("sandbox",true);
        payment.addProperty("merchant_id",merchant_id);
        payment.addProperty("return_url","index.html");
        payment.addProperty("cancel_url","index.html");
        payment.addProperty("notify_url","index.html");
        payment.addProperty("first_name",user.getFirstName());
        payment.addProperty("last_name",user.getLastName());
        payment.addProperty("email",user.getEmail());
        payment.addProperty("phone",order.getAddress().getMobile());
        payment.addProperty("address",order.getAddress().getLine1());
        payment.addProperty("city",order.getAddress().getCity().getName());
        payment.addProperty("country","Sri Lanka");
        payment.addProperty("order_id",order_id);
        payment.addProperty("items",items);
        payment.addProperty("currency","LKR");
        payment.addProperty("amount",format);
        String md5 = PayHere.getMd5(merchant_id+order_id+format+"LKR"+merchantSecretmd5Hash);
        payment.addProperty("hash",md5);

        responseJsonObject.add("payment",new Gson().toJsonTree(payment) );
        responseJsonObject.addProperty("message", "Success");
        responseJsonObject.addProperty("success", true);
    }

}

//naththan eliyen address variable ekk hadala  othanata awama ekata assign karala eliyen ithuru process eka karaganna puluwan neda sir
