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
import java.text.NumberFormat;
import java.util.Locale;

@WebServlet(name = "AdminCustomerController", value = "/admin/customers")
public class AdminCustomerController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDao uDao = new UserDao();
        OrderDao oDao = new OrderDao();

        String keyword = request.getParameter("keyword");
        keyword = keyword == null ? "" : keyword.trim();

        List<User> customers = uDao.searchCustomers(keyword);

        request.setAttribute("keyword", keyword);
        List<Map<String, Object>> latestNotifications = oDao.getLatestAdminNotifications(5);

        request.setAttribute("customers", customers);
        request.setAttribute("totalCustomers", uDao.countTotalCustomers());
        request.setAttribute("vipCustomers", uDao.countVipCustomers());
        request.setAttribute("newCustomersThisMonth", uDao.countNewCustomersThisMonth());
        request.setAttribute("averageSpendFormatted", formatCurrency(uDao.getAverageSpendPerCustomer()));
        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("latestNotifications", latestNotifications);

        request.getRequestDispatcher("/jsp/adminjsp/Admin_KhachHang.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
    private String formatCurrency(double value) {
        NumberFormat vn = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return vn.format(value);
    }
}