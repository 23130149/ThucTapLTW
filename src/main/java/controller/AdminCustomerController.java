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
        String customerType = request.getParameter("customerType");
        String orderRange = request.getParameter("orderRange");
        String detailIdRaw = request.getParameter("detailId");

        keyword = keyword == null ? "" : keyword.trim();
        customerType = customerType == null ? "" : customerType.trim();
        orderRange = orderRange == null ? "" : orderRange.trim();
        User selectedCustomer = null;

        int[] orderRangeValue = parseOrderRange(orderRange);
        int minOrders = orderRangeValue[0];
        int maxOrders = orderRangeValue[1];

        if (detailIdRaw != null && !detailIdRaw.isBlank()) {
            try {
                int detailId = Integer.parseInt(detailIdRaw);
                selectedCustomer = uDao.getCustomerDetail(detailId);
            } catch (NumberFormatException ignored) {
            }
        }

        List<User> customers = uDao.filterCustomers(keyword, customerType, minOrders, maxOrders);

        List<Map<String, Object>> latestNotifications = oDao.getLatestAdminNotifications(5);

        request.setAttribute("customers", customers);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentCustomerType", customerType);
        request.setAttribute("currentOrderRange", orderRange);
        request.setAttribute("totalCustomers", uDao.countTotalCustomers());
        request.setAttribute("vipCustomers", uDao.countVipCustomers());
        request.setAttribute("newCustomersThisMonth", uDao.countNewCustomersThisMonth());
        request.setAttribute("averageSpendFormatted", formatCurrency(uDao.getAverageSpendPerCustomer()));
        request.setAttribute("selectedCustomer", selectedCustomer);
        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("latestNotifications", latestNotifications);

        request.getRequestDispatcher("/jsp/adminjsp/Admin_KhachHang.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        UserDao uDao = new UserDao();
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            try {
                int userId = Integer.parseInt(request.getParameter("userId"));
                uDao.deleteCustomer(userId);
            } catch (NumberFormatException ignored) {
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/customers");
    }

    private String formatCurrency(double value) {
        NumberFormat vn = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return vn.format(value);
    }
    private int[] parseOrderRange(String orderRange) {
        return switch (orderRange) {
            case "0" -> new int[]{0, 0};
            case "1-5" -> new int[]{1, 5};
            case "6-10" -> new int[]{6, 10};
            case "11+" -> new int[]{11, -1};
            default -> new int[]{-1, -1};
        };
    }
}