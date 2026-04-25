package controller;

import dao.OrderDao;
import dao.ProductDao;
import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Order;
import model.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminDashboardController", value = "/admin/dashboard")
public class AdminDashboardController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao oDao = new OrderDao();
        UserDao uDao = new UserDao();
        ProductDao pDao = new ProductDao();

    String range = request.getParameter("range");
    int days = ("30".equals(range)) ? 30 : 7;

    double totalRevenue = oDao.getTotalRevenue();
    int totalOrders = oDao.countOrders();
    int totalUsers = uDao.countUsers();


    List<Product> topProducts = pDao.getTopProducts(5);

    List<Order> latestOrders = oDao.getLatestOrders(5);

        if (topProducts == null) topProducts = new ArrayList<>();
        if (latestOrders == null) latestOrders = new ArrayList<>();

        request.setAttribute("range", days);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("topProducts", topProducts);
        request.setAttribute("latestOrders", latestOrders);
        request.getRequestDispatcher("/jsp/adminjsp/Admin_TongQuan.jsp")
                .forward(request, response);
}

@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}