package controller;

import dao.OrderDao;
import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminCustomerController", value = "/admin/customers")
public class AdminCustomerController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDao userDao = new UserDao();
        OrderDao orderDao = new OrderDao();

        List<User> customers = userDao.getAllCustomers();
        List<Map<String, Object>> latestNotifications = orderDao.getLatestAdminNotifications(5);

        request.setAttribute("customers", customers);
        request.setAttribute("notificationCount", orderDao.countAdminNotifications());
        request.setAttribute("latestNotifications", latestNotifications);

        request.getRequestDispatcher("/jsp/adminjsp/Admin_KhachHang.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}