package controller;

import dao.UserDao;
import model.User;
import java.time.LocalDate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/Profile/Edit")
public class ProfileEdit extends HttpServlet {

    private UserDao userDao = new UserDao();

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
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        user.setUserName(request.getParameter("userName"));
        user.setPhone(request.getParameter("phone"));
        user.setDateOfBirth(null);
        String dob = request.getParameter("dateOfBirth");
        if (dob != null && !dob.isBlank()) {
            user.setDateOfBirth(LocalDate.parse(dob));
        }

        user.setGender(request.getParameter("gender"));
        user.setAvatarUrl(request.getParameter("avatarUrl"));
        user.setBio(request.getParameter("bio"));

        userDao.updateProfile(user);
        session.setAttribute("user", user);

        response.sendRedirect(request.getContextPath() + "/Profile");
    }
}
