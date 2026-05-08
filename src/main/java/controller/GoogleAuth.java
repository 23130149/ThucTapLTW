package controller;

import dao.UserDao;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/GoogleAuth")
public class GoogleAuth extends HttpServlet {

    private UserDao userDao;
    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String credential = request.getParameter("credential");

        if (credential == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        String[] parts = credential.split("\\.");
        String payloadJson = new String(
                java.util.Base64.getUrlDecoder().decode(parts[1])
        );

        com.google.gson.JsonObject json =
                com.google.gson.JsonParser.parseString(payloadJson).getAsJsonObject();

        String email = json.get("email").getAsString();
        String googleId = json.get("sub").getAsString();

        User user = userDao.findByEmail(email);

        if (user == null) {
            userDao.insertGoogleUser(email, googleId);
            user = userDao.findByEmail(email);
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/adminjsp/Admin_Tongquan.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/Account");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
