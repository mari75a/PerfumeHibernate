/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ResponseDto;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "SignOut", urlPatterns = {"/SignOut"})
public class SignOut extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession(false);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        ResponseDto response_DTO = new ResponseDto();
        if(session != null &&session.getAttribute("user") != null){
            session.invalidate();
            response_DTO.setSuccess(true);
                    response_DTO.setContent("SignOut  success");
            
        }else{
        response_DTO.setSuccess(false);
                    response_DTO.setContent("SignOut  Failed");
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(response_DTO));
    }

    
    
}
