/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.ResponseDto;
import dto.UserDto;
import entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Verification", urlPatterns = {"/Verification"})
public class Verification extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject dto = gson.fromJson(req.getReader(), JsonObject.class);

        ResponseDto response_DTO = new ResponseDto();

        String Verification = dto.get("verification").getAsString();

        if (req.getSession().getAttribute("email") != null) {

            String email = req.getSession().getAttribute("email").toString();

            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", email));
            criteria1.add(Restrictions.eq("verification", Verification));

            if (!criteria1.list().isEmpty()) {

                User user = (User) criteria1.list().get(0);
                user.setVerification("Verified");

                session.update(user);
                session.beginTransaction().commit();

                UserDto user_DTO = new UserDto();

                user_DTO.setFirstname(user.getFirstName());
                user_DTO.setLastname(user.getLastName());
                user_DTO.setEmail(user.getEmail());
                user_DTO.setPassword(null);
                
                req.getSession().removeAttribute("email");
                req.getSession().setAttribute("user", user_DTO);

                response_DTO.setSuccess(true);
                response_DTO.setContent("Verification success");

            } else {
                response_DTO.setContent("Invalid Verification code !!!");
            }

        } else {
            response_DTO.setContent("Verification unavailable !! Please Sign in again");
        }
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(response_DTO));
    }

}
