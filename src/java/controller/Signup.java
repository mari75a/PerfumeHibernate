package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.tools.ws.processor.modeler.wsdl.ConsoleErrorReporter;
import dto.ResponseDto;
import dto.UserDto;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity.User;
import java.io.Console;
import model.HibernateUtil;
import model.Mail;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author chand
 */
@WebServlet(name = "Signup", urlPatterns = {"/Signup"})
public class Signup extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

         UserDto user_DTO = gson.fromJson(request.getReader(), UserDto.class);

        ResponseDto response_DTO = new ResponseDto();

        if (user_DTO.getFirstname().isEmpty()) {
            response_DTO.setContent("Please enter your First Name");
        } else if (user_DTO.getLastname().isEmpty()) {
            response_DTO.setContent("Please enter your Last Name");
        } else if (user_DTO.getEmail().isEmpty()) {
            response_DTO.setContent("Please enter your Email");
        } else if (!Validation.isEmailValid(user_DTO.getEmail())) {
            response_DTO.setContent("Email is not valid.");
        } else if (user_DTO.getPassword().isEmpty()) {
            response_DTO.setContent("Please Enter your Password");
        } else if (!Validation.isPasswordValid(user_DTO.getPassword())) {
            response_DTO.setContent("Password must include ate leat one uppercase letter, number,special charactor and be at least eight chracters long.");
        } else {

            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
           
            if (!criteria1.list().isEmpty()) {
                response_DTO.setContent("Email already used by another user.");

            } else {

                int code = (int) (Math.random() * 100000);

                final User user = new User();
                user.setEmail(user_DTO.getEmail());
                user.setFirstName(user_DTO.getFirstname());
                user.setLastName(user_DTO.getLastname());
                user.setPassword(user_DTO.getPassword());
                user.setVerification(String.valueOf(code));

                String content = "<html lang=\"en\">\n"
                        + "<head>\n"
                        + "    <meta charset=\"UTF-8\">\n"
                        + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                        + "    <title>Verify Your Email</title>\n"
                        + "</head>\n"
                        + "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0;\">\n"
                        + "    <table align=\"center\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">\n"
                        + "        <tr>\n"
                        + "            <td style=\"text-align: center;\">\n"
                        + "                <h2 style=\"color: #333333;\">Verify Your Email</h2>\n"
                        + "                <p style=\"color: #666666;\">Thank you for signing up with Perfumeria!</p>\n"
                        + "                <p style=\"color: #666666;\">Please use the verification code below to complete your registration:</p>\n"
                        + "                <h3 style=\"color: #FFC0CB; font-size: 24px; margin: 20px 0;\">" + code + "</h3>\n"
                        + "                <p style=\"color: #666666;\">If you didn’t request this, you can safely ignore this email.</p>\n"
                        + "                <p style=\"color: #666666;\">Contact Us on Facebook <b style=\"color: #007bff;\">(Softmatic Solutions)</b></p>\n"
                        + "            </td>\n"
                        + "        </tr>\n"
                        + "    </table>\n"
                        + "</body>\n"
                        + "</html>";

                Thread sendMailThread = new Thread() {
                    @Override
                    public void run() {
                        Mail.sendMail(user_DTO.getEmail(), "Verification Smart Trade Account", content);
                    }
                };

                sendMailThread.start();

                session.save(user);
                session.beginTransaction().commit();
                request.getSession().setAttribute("email", user_DTO.getEmail());
                response_DTO.setSuccess(true);

                response_DTO.setContent("Registration complete");
            }
            session.close();
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        

    }

}
