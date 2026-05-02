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

    @WebServlet(name = "AdminDashboardController")
    public class AdminDashboardController extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            OrderDao oDao = new OrderDao();
            UserDao uDao = new UserDao();
            ProductDao pDao = new ProductDao();

        String range = request.getParameter("range");
        if (range == null || (!range.equals("7") && !range.equals("30"))) {
            range = "7";
        }

        double totalRevenue = oDao.getTotalRevenue();
        int totalOrders = oDao.countOrders();
        int totalUsers = uDao.countUsers();

        List<Map<String, Object>> revenueChart = oDao.getRevenueChart(range);
            double max = 0;
            for (Map<String, Object> item : revenueChart) {
                double value = (double) item.get("value");
                if (value > max) max = value;
            }

            if (max == 0) {
                for (Map<String, Object> item : revenueChart) {
                    item.put("percent", 5);
                }
            } else {
                for (Map<String, Object> item : revenueChart) {
                    double value = (double) item.get("value");
                    item.put("percent", (value / max) * 100);
                }
            }

        List<Product> topProducts = pDao.getTopProducts(5);
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
            request.getRequestDispatcher("/jsp/adminjsp/Admin_TongQuan.jsp").forward(request, response);
    }

    @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        }
    }