package controller;

import dao.UserDao;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.EmailService;
import service.OtpService;
import util.PasswordUtil;

import java.io.IOException;

@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {

    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        if (user.getGoogleId() != null) {
            response.sendRedirect(request.getContextPath() + "/Account");
            return;
        }

        request.getRequestDispatcher("/jsp/changepassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        if (user.getGoogleId() != null) {
            response.sendRedirect(request.getContextPath() + "/Account");
            return;
        }

        String action = request.getParameter("action");

        if ("sendOtp".equals(action)) {

            String oldPassword = request.getParameter("oldPassword");
            boolean passwordVerified = Boolean.TRUE.equals(
                    session.getAttribute("CHANGE_PASSWORD_VERIFIED")
            );

            if (!passwordVerified) {
                if (oldPassword == null || !PasswordUtil.verify(oldPassword, user.getPassword())) {
                    request.setAttribute("error", "Mật khẩu hiện tại không đúng");
                    request.getRequestDispatcher("/jsp/changepassword.jsp")
                            .forward(request, response);
                    return;
                }
                session.setAttribute("CHANGE_PASSWORD_VERIFIED", true);
            }

            String otp = OtpService.generateOtp();
            OtpService.saveOtp(session, otp);
            EmailService.sendOtpEmail(user.getEmail(), otp);

            request.setAttribute("step", "OTP_SENT");
            request.setAttribute("resendRemain",
                    OtpService.getResendRemain(session));
            request.getRequestDispatcher("/jsp/changepassword.jsp")
                    .forward(request, response);
            return;
        }

        if ("confirm".equals(action)) {

            String otpInput = request.getParameter("otp");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
                request.setAttribute("error", "Mật khẩu xác nhận không khớp");
                request.setAttribute("step", "OTP_SENT");
                request.setAttribute("resendRemain",
                        OtpService.getResendRemain(session));
                request.getRequestDispatcher("/jsp/changepassword.jsp")
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
                request.setAttribute("resendRemain",
                        OtpService.getResendRemain(session));
                request.getRequestDispatcher("/jsp/changepassword.jsp")
                        .forward(request, response);
                return;
            }

            if (!OtpService.verifyOtp(session, otpInput)) {
                request.setAttribute("error", "OTP sai hoặc đã hết hạn");
                request.setAttribute("step", "OTP_SENT");
                request.setAttribute("resendRemain",
                        OtpService.getResendRemain(session));
                request.getRequestDispatcher("/jsp/changepassword.jsp")
                        .forward(request, response);
                return;
            }

            String hashedPassword = PasswordUtil.hash(newPassword);
            userDao.updatePassword(user.getUserId(), hashedPassword);

            user.setPassword(hashedPassword);
            session.setAttribute("user", user);

            OtpService.clearOtp(session);
            session.removeAttribute("CHANGE_PASSWORD_VERIFIED");
            response.sendRedirect(request.getContextPath() + "/Account");
        }
    }
}
