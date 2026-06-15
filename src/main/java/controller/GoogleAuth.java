package controller;

import dao.CartDao;
import dao.UserDao;
import model.User;
import util.GoogleTokenVerifier;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/GoogleAuth")
public class GoogleAuth extends HttpServlet {

    private UserDao userDao;
    private CartDao cartDao;
    @Override
    public void init() {
        userDao = new UserDao();
        cartDao = new CartDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String credential = request.getParameter("credential");

        if (credential == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        HttpSession session = request.getSession();
        User user;
        try {
            com.google.gson.JsonObject json = GoogleTokenVerifier.verify(credential, getServletContext());
            String email = json.get("email").getAsString();
            String googleId = json.get("sub").getAsString();
            String userName = json.has("name") && !json.get("name").isJsonNull()
                    ? json.get("name").getAsString()
                    : null;

            user = userDao.findByEmail(email);
            if (user == null) {
                userDao.insertGoogleUser(email, googleId, userName);
                user = userDao.findByEmail(email);
            }
        } catch (Exception e) {
            session.setAttribute("loginMessage", "Không thể xác minh tài khoản Google. Vui lòng thử lại.");
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        if (user == null) {
            session.setAttribute("loginMessage", "Không thể tạo tài khoản Google.");
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        session.setAttribute("cart", cartDao.getCartByUserId(user.getUserId()));
        session.setAttribute("user", user);

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
            session.removeAttribute("redirectAfterLogin");

            if (redirectAfterLogin != null && !redirectAfterLogin.isBlank()
                    && !redirectAfterLogin.startsWith("http://")
                    && !redirectAfterLogin.startsWith("https://")
                    && !redirectAfterLogin.startsWith("//")) {
                while (redirectAfterLogin.startsWith("/")) {
                    redirectAfterLogin = redirectAfterLogin.substring(1);
                }
                response.sendRedirect(request.getContextPath() + "/" + redirectAfterLogin);
            } else {
                response.sendRedirect(request.getContextPath() + "/Account");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
