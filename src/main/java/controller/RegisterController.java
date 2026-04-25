package controller;

import dao.UserDao;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import service.EmailService;
import util.PasswordUtil;

import java.io.IOException;

@WebServlet("/Register")
public class RegisterController extends HttpServlet {

    private static final int OTP_EXPIRE_SECONDS = 120;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String action = request.getParameter("action");
        UserDao userDao = new UserDao();

        if ("sendOtp".equals(action)) {

            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");

            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");

            if (password == null) {
                fullName = (String) session.getAttribute("reg_fullName");
                email = (String) session.getAttribute("reg_email");

                if (fullName == null || email == null) {
                    request.setAttribute("error", "Phiên đăng ký đã hết hạn, vui lòng đăng ký lại");
                    request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                    return;
                }
            }
            else {
                if (!password.equals(confirmPassword)) {
                    request.setAttribute("error", "Mật khẩu xác nhận không khớp");
                    request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                    return;
                }

                if (!email.endsWith("@gmail.com")) {
                    request.setAttribute("error", "Chỉ chấp nhận Gmail");
                    request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                    return;
                }

                if (userDao.emailExists(email)) {
                    request.setAttribute("error", "Email đã tồn tại");
                    request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                    return;
                }

                String hashedPassword = PasswordUtil.hash(password);
                session.setAttribute("reg_password", hashedPassword);
                session.setAttribute("reg_fullName", fullName);
                session.setAttribute("reg_email", email);
            }

            String otp = String.valueOf(100000 + (int) (Math.random() * 900000));
            EmailService.sendOtpEmail(email, otp);

            session.setAttribute("reg_otp", otp);
            session.setAttribute("reg_otp_time", System.currentTimeMillis());

            request.setAttribute("step", "OTP_SENT");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }

        if ("confirmOtp".equals(action)) {

            String inputOtp = request.getParameter("otp");

            String sessionOtp = (String) session.getAttribute("reg_otp");
            Long otpTime = (Long) session.getAttribute("reg_otp_time");

            if (sessionOtp == null || otpTime == null) {
                request.setAttribute("error", "OTP không hợp lệ hoặc đã hết hạn");
                request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                return;
            }

            long diff = (System.currentTimeMillis() - otpTime) / 1000;
            if (diff > OTP_EXPIRE_SECONDS) {
                request.setAttribute("error", "OTP đã hết hạn");
                request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                return;
            }

            if (!sessionOtp.equals(inputOtp)) {
                request.setAttribute("error", "OTP không đúng");
                request.setAttribute("step", "OTP_SENT");
                request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                return;
            }

            User user = new User();
            user.setUserName((String) session.getAttribute("reg_fullName"));
            user.setEmail((String) session.getAttribute("reg_email"));
            user.setPassword((String) session.getAttribute("reg_password"));
            user.setRole("USER");
            user.setPhone(null);

            userDao.register(user);

            session.removeAttribute("reg_fullName");
            session.removeAttribute("reg_email");
            session.removeAttribute("reg_password");
            session.removeAttribute("reg_otp");
            session.removeAttribute("reg_otp_time");

            response.sendRedirect(request.getContextPath() + "/jsp/signin.jsp?success=1");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/jsp/register.jsp")
                .forward(request, response);
    }
}
