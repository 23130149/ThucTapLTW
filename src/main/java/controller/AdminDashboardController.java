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
    import java.util.Map;

    @WebServlet(name = "AdminDashboardController", value = "/admin/dashboard")

    public class AdminDashboardController extends HttpServlet {
        private static final String COMPLETED_STATUS = "COMPLETED";

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            OrderDao oDao = new OrderDao();
            UserDao uDao = new UserDao();
            ProductDao pDao = new ProductDao();

        String range = request.getParameter("range");
        if (range == null || (!range.equals("7") && !range.equals("30"))) {
            range = "7";
        }

            double totalRevenue = oDao.getTotalRevenueByStatus(COMPLETED_STATUS);
            int totalOrders = oDao.countOrdersByStatus(COMPLETED_STATUS);
            int totalUsers = uDao.countUsers();

            List<Map<String, Object>> revenueChart = oDao.getRevenueChartByStatus(range, COMPLETED_STATUS);

            double max = 0;
            for (Map<String, Object> item : revenueChart) {
                double value = (double) item.get("value");
                if (value > max) {
                    max = value;
                }
            }

            for (Map<String, Object> item : revenueChart) {
                double value = (double) item.get("value");
                item.put("percent", max == 0 ? 5 : Math.max((value / max) * 100, 18));
            }

            List<Product> topProducts = pDao.getTopProductsByStatus(5, COMPLETED_STATUS);
            if (topProducts == null) topProducts = new ArrayList<>();


            List<Order> latestOrders = oDao.getLatestOrders(5);
        if (latestOrders == null) latestOrders = new ArrayList<>();



            request.setAttribute("range", range);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("revenueChart", revenueChart);
            request.setAttribute("topProducts", topProducts);
            request.setAttribute("latestOrders", latestOrders);
            request.setAttribute("notificationCount", oDao.countAdminNotifications());
            request.setAttribute("latestNotifications", oDao.getLatestAdminNotifications(5));

            request.getRequestDispatcher("/jsp/adminjsp/Admin_TongQuan.jsp").forward(request, response);
    }

    @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        }
    }