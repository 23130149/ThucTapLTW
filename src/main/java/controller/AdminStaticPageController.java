package controller;

import dao.ContactDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(
        name = "AdminStaticPageController",
        value = {
                "/admin/banner",
                "/admin/contacts",
                "/admin/notifications"
        }
)public class AdminStaticPageController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();


        if ("/admin/banner".equals(path)) {
            request.getRequestDispatcher("/jsp/adminjsp/Admin_Banner.jsp").forward(request, response);
            return;
        }

        if ("/admin/contacts".equals(path)) {
            ContactDao contactDao = new ContactDao();
            String keyword = request.getParameter("keyword");
            request.setAttribute("contacts", keyword == null || keyword.trim().isEmpty() ? contactDao.findAll() : contactDao.search(keyword.trim()));
            request.setAttribute("totalContacts", contactDao.count());
            request.setAttribute("keyword", keyword == null ? "" : keyword);
            request.getRequestDispatcher("/jsp/adminjsp/Admin_LienHe.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
    }
}
