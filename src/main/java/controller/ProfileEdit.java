package controller;

import dao.UserDao;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/Profile/Edit")
public class ProfileEdit extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        request.getRequestDispatcher("/jsp/profile_edit.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User currentUser = session == null ? null : (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        Map<String, String> errors = new LinkedHashMap<>();

        String userName = normalize(request.getParameter("userName"));
        String email = normalize(request.getParameter("email"));
        String phone = normalize(request.getParameter("phone"));
        String dob = normalize(request.getParameter("dateOfBirth"));
        String gender = normalize(request.getParameter("gender"));
        String avatarUrl = normalize(request.getParameter("avatarUrl"));
        String bio = normalize(request.getParameter("bio"));

        if (userName.isBlank()) {
            errors.put("userName", "Vui lòng nhập họ và tên.");
        } else if (userName.length() < 2 || userName.length() > 60) {
            errors.put("userName", "Họ và tên phải từ 2 đến 60 ký tự.");
        } else if (!userName.matches("^[\\p{L} ]+$")) {
            errors.put("userName", "Họ và tên chỉ nên chứa chữ cái và khoảng trắng.");
        }

        boolean currentEmailEmpty = isBlank(currentUser.getEmail());
        if (!currentEmailEmpty) {
            email = currentUser.getEmail();
        } else if (!email.isBlank()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                errors.put("email", "Email không đúng định dạng.");
            } else if (userDao.emailExistsExceptUser(email, currentUser.getUserId())) {
                errors.put("email", "Email này đã được sử dụng.");
            }
        }

        if (!phone.isBlank() && !phone.matches("^0[0-9]{9,10}$")) {
            errors.put("phone", "Số điện thoại phải bắt đầu bằng 0 và gồm 10 đến 11 số.");
        }

        LocalDate dateOfBirth = null;
        if (!dob.isBlank()) {
            try {
                dateOfBirth = LocalDate.parse(dob);
                if (dateOfBirth.isAfter(LocalDate.now())) {
                    errors.put("dateOfBirth", "Ngày sinh không được lớn hơn ngày hiện tại.");
                }
            } catch (DateTimeParseException e) {
                errors.put("dateOfBirth", "Ngày sinh không hợp lệ.");
            }
        }

        if (!gender.isBlank()
                && !"MALE".equals(gender)
                && !"FEMALE".equals(gender)
                && !"OTHER".equals(gender)) {
            errors.put("gender", "Giới tính không hợp lệ.");
        }

        if (!avatarUrl.isBlank()
                && !avatarUrl.matches("^(https?://).+")) {
            errors.put("avatarUrl", "Ảnh đại diện phải là đường dẫn bắt đầu bằng http:// hoặc https://.");
        }

        if (bio.length() > 180) {
            errors.put("bio", "Giới thiệu ngắn không nên vượt quá 180 ký tự.");
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin đã nhập.");
            request.getRequestDispatcher("/jsp/profile_edit.jsp")
                    .forward(request, response);
            return;
        }

        currentUser.setUserName(userName);
        currentUser.setEmail(email.isBlank() ? null : email);
        currentUser.setPhone(phone.isBlank() ? null : phone);
        currentUser.setDateOfBirth(dateOfBirth);
        currentUser.setGender(gender.isBlank() ? null : gender);
        currentUser.setAvatarUrl(avatarUrl.isBlank() ? null : avatarUrl);
        currentUser.setBio(bio.isBlank() ? null : bio);

        userDao.updateProfile(currentUser);
        session.setAttribute("user", currentUser);
        session.setAttribute("profileMessage", "Cập nhật thông tin thành công.");

        response.sendRedirect(request.getContextPath() + "/Profile");
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().replaceAll("\\s+", " ");
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
