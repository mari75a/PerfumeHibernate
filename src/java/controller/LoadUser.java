/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.ResponseDto;
import dto.UserDto;
import entity.Cart;
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
import javax.servlet.jsp.tagext.TryCatchFinally;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sange
 */
@WebServlet(name = "LoadUser", urlPatterns = {"/LoadUser"})
public class LoadUser extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        ResponseDto responseDto = new ResponseDto();
        if (req.getSession().getAttribute("user") != null) {
            //db cart
            UserDto userDto = (UserDto) req.getSession().getAttribute("user");

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", userDto.getEmail()));
            User user = (User) criteria1.uniqueResult();

            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(user));

        }

    }

}
