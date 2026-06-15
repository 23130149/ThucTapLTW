package controller;

import dao.UserDao;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.EmailService;
import util.PasswordUtil;
import util.RecaptchaUtil;

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
                    prepareRecaptcha(request);
                    request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                    return;
                }

                if (RecaptchaUtil.isConfigured(getServletContext())
                        && !RecaptchaUtil.verify(request, getServletContext())) {
                    request.setAttribute("error", "Vui lòng xác nhận bạn không phải robot.");
                    request.setAttribute("step", "OTP_SENT");
                    prepareRecaptcha(request);
                    request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                    return;
                }
            }
            else {
                fullName = normalize(fullName);
                email = normalize(email).toLowerCase();

                if (RecaptchaUtil.isConfigured(getServletContext())
                        && !RecaptchaUtil.verify(request, getServletContext())) {
                    request.setAttribute("error", "Vui lòng xác nhận bạn không phải robot.");
                    prepareRecaptcha(request);
                    request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                    return;
                }

                if (!isValidFullName(fullName)) {
                    request.setAttribute("error", "Họ tên phải có ít nhất 2 ký tự và không chứa ký tự đặc biệt.");
                    prepareRecaptcha(request);
                    request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                    return;
                }

                if (!isStrongPassword(password)) {
                    request.setAttribute("error", "Mật khẩu phải có ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
                    prepareRecaptcha(request);
                    request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    request.setAttribute("error", "Mật khẩu xác nhận không khớp");
                    prepareRecaptcha(request);
                    request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                    return;
                }

                if (!email.endsWith("@gmail.com")) {
                    request.setAttribute("error", "Chỉ chấp nhận Gmail");
                    prepareRecaptcha(request);
                    request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                    return;
                }

                if (userDao.emailExists(email)) {
                    request.setAttribute("error", "Email đã tồn tại");
                    prepareRecaptcha(request);
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

            response.sendRedirect(request.getContextPath() + "/SignIn?success=1");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        prepareRecaptcha(request);
        request.getRequestDispatcher("/jsp/register.jsp")
                .forward(request, response);
    }

    private void prepareRecaptcha(HttpServletRequest request) {
        request.setAttribute("recaptchaSiteKey", RecaptchaUtil.getSiteKey(getServletContext()));
        request.setAttribute("recaptchaConfigured", RecaptchaUtil.isConfigured(getServletContext()));
    }

    private boolean isStrongPassword(String password) {
        return password != null
                && password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*\\d.*")
                && password.matches(".*[^A-Za-z0-9].*");
    }

    private boolean isValidFullName(String fullName) {
        return fullName != null
                && fullName.length() >= 2
                && fullName.length() <= 100
                && fullName.matches("[\\p{L} .'-]+");
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().replaceAll("\\s+", " ");
    }
}
