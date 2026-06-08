package controller;

import dao.ContactDao;
import dao.OrderDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Contact;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminContactController", value = "/admin/contacts")
public class AdminContactController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ContactDao ctDao = new ContactDao();
        OrderDao oDao = new OrderDao();

        String detailIdRaw = request.getParameter("detailId");
        Contact selectedContact = null;

        if (detailIdRaw != null && !detailIdRaw.isBlank()) {
            try {
                int detailId = Integer.parseInt(detailIdRaw);
                selectedContact = ctDao.findById(detailId);
            } catch (NumberFormatException ignored) {
            }
        }

        List<Contact> contacts = ctDao.findAll();

        request.setAttribute("contacts", contacts);
        request.setAttribute("selectedContact", selectedContact);
        request.setAttribute("totalContacts", ctDao.count());
        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("latestNotifications", oDao.getLatestAdminNotifications(5));

        request.getRequestDispatcher("/jsp/adminjsp/Admin_LienHe.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/admin/contacts");
    }
}