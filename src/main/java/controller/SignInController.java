package controller;

import cart.Cart;
import dao.CartDao;
import dao.UserDao;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.PasswordUtil;
import util.RecaptchaUtil;

import java.io.IOException;

@WebServlet("/SignIn")
public class SignInController extends HttpServlet {
    private UserDao userDao;
    private CartDao cartDao;

    @Override
    public void init() {
        userDao = new UserDao();
        cartDao = new CartDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.trim().isEmpty()) {
            request.getSession().setAttribute("redirectAfterLogin", sanitizeRedirect(redirect));
        }

        prepareRecaptcha(request, false);
        request.getRequestDispatcher("/jsp/signin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("pass");
        HttpSession session = request.getSession();

        if (RecaptchaUtil.isConfigured(getServletContext())
                && !RecaptchaUtil.verify(request, getServletContext())) {
            request.setAttribute("error", "Vui lòng xác nhận bạn không phải robot.");
            prepareRecaptcha(request, true);
            request.getRequestDispatcher("/jsp/signin.jsp").forward(request, response);
            return;
        }

        User user = userDao.findByEmail(email);

        if (user == null || user.getPassword() == null
                || !PasswordUtil.verify(password, user.getPassword())) {
            request.setAttribute("error", "Sai email hoặc mật khẩu");
            prepareRecaptcha(request, false);
            request.getRequestDispatcher("/jsp/signin.jsp").forward(request, response);
            return;
        }

        Cart sessionCart = (Cart) session.getAttribute("cart");
        cartDao.mergeSessionCartToDb(user.getUserId(), sessionCart);
        session.setAttribute("cart", cartDao.getCartByUserId(user.getUserId()));
        session.setAttribute("user", user);

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            String redirectAfterLogin = sanitizeRedirect((String) session.getAttribute("redirectAfterLogin"));
            session.removeAttribute("redirectAfterLogin");

            if (redirectAfterLogin != null && !redirectAfterLogin.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/" + redirectAfterLogin);
            } else {
                response.sendRedirect(request.getContextPath() + "/Account");
            }
        }
    }

    private String sanitizeRedirect(String redirect) {
        if (redirect == null) {
            return null;
        }

        String value = redirect.trim();

        if (value.startsWith("http://") || value.startsWith("https://") || value.startsWith("//")) {
            return null;
        }

        while (value.startsWith("/")) {
            value = value.substring(1);
        }

        return value.isBlank() ? null : value;
    }

    private void prepareRecaptcha(HttpServletRequest request, boolean visible) {
        request.setAttribute("recaptchaSiteKey", RecaptchaUtil.getSiteKey(getServletContext()));
        request.setAttribute("recaptchaConfigured", RecaptchaUtil.isConfigured(getServletContext()));
        request.setAttribute("recaptchaVisible", visible && RecaptchaUtil.isConfigured(getServletContext()));
    }
}
