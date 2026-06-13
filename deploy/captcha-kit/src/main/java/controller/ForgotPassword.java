package controller;

import dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import service.EmailService;
import service.OtpService;
import util.PasswordUtil;
import util.RecaptchaUtil;

import java.io.IOException;

@WebServlet("/ForgotPassword")
public class ForgotPassword extends HttpServlet {

    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        if ("sendOtp".equals(action)) {

            String email = request.getParameter("email");
            if (email == null || email.isBlank()) {
                email = (String) session.getAttribute("OTP_EMAIL");
            }

            if (email == null || email.isBlank()) {
                request.setAttribute("error", "Vui lòng nhập email.");
                prepareRecaptcha(request);
                request.getRequestDispatcher("/jsp/forgotpassword.jsp")
                        .forward(request, response);
                return;
            }

            if (RecaptchaUtil.isConfigured(getServletContext())
                    && !RecaptchaUtil.verify(request, getServletContext())) {
                request.setAttribute("error", "Vui lòng xác nhận bạn không phải robot.");
                request.setAttribute("step", session.getAttribute("OTP_EMAIL") == null ? null : "OTP_SENT");
                prepareRecaptcha(request);
                request.getRequestDispatcher("/jsp/forgotpassword.jsp")
                        .forward(request, response);
                return;
            }

            String otp = OtpService.generateOtp();
            OtpService.saveOtp(session, otp);
            session.setAttribute("OTP_EMAIL", email);

            EmailService.sendOtpEmail(email, otp);

            request.setAttribute("step", "OTP_SENT");
            prepareRecaptcha(request);
            request.getRequestDispatcher("/jsp/forgotpassword.jsp")
                    .forward(request, response);
            return;
        }

        if ("confirm".equals(action)) {

            String otpInput = request.getParameter("otp");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            String email = (String) session.getAttribute("OTP_EMAIL");

            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("error", "Mật khẩu xác nhận không khớp");
                request.setAttribute("step", "OTP_SENT");
                prepareRecaptcha(request);
                request.getRequestDispatcher("/jsp/forgotpassword.jsp")
                        .forward(request, response);
                return;
            }

            boolean strong =
                    newPassword.length() >= 8 &&
                            newPassword.matches(".*[A-Z].*") &&
                            newPassword.matches(".*[a-z].*") &&
                            newPassword.matches(".*\\d.*") &&
                            newPassword.matches(".*[^A-Za-z0-9].*");

            if (!strong) {
                request.setAttribute("error", "Mật khẩu chưa đủ mạnh");
                request.setAttribute("step", "OTP_SENT");
                prepareRecaptcha(request);
                request.getRequestDispatcher("/jsp/forgotpassword.jsp")
                        .forward(request, response);
                return;
            }

            if (!OtpService.verifyOtp(session, otpInput)) {
                request.setAttribute("error", "OTP sai hoặc đã hết hạn");
                request.setAttribute("step", "OTP_SENT");
                prepareRecaptcha(request);
                request.getRequestDispatcher("/jsp/forgotpassword.jsp")
                        .forward(request, response);
                return;
            }

            User user = userDao.findByEmail(email);

            if (user != null && user.getGoogleId() != null) {
                request.setAttribute("error",
                        "Tài khoản đăng nhập bằng Google/Facebook không hỗ trợ quên mật khẩu");
                request.getRequestDispatcher("/jsp/signIn.jsp")
                        .forward(request, response);
                return;
            }

            String hashedPassword = PasswordUtil.hash(newPassword);
            userDao.updatePassword(user.getUserId(), hashedPassword);

            OtpService.clearOtp(session);
            session.removeAttribute("OTP_EMAIL");

            response.sendRedirect(request.getContextPath() + "/SignIn");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        prepareRecaptcha(request);
        request.getRequestDispatcher("/jsp/forgotpassword.jsp")
                .forward(request, response);
    }

    private void prepareRecaptcha(HttpServletRequest request) {
        request.setAttribute("recaptchaSiteKey", RecaptchaUtil.getSiteKey(getServletContext()));
        request.setAttribute("recaptchaConfigured", RecaptchaUtil.isConfigured(getServletContext()));
    }
}
