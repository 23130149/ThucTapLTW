package controller;

import dao.OrderDao;
import dao.OrderItemDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Order;
import model.OrderItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "AdminOrderController", value = "/admin/orders")
public class AdminOrderController extends HttpServlet {
    private static final Set<String> VALID_STATUSES = Set.of(
            "PENDING",
            "CONFIRMED",
            "SHIPPED",
            "COMPLETED",
            "CANCELLED"
    );
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao orderDao = new OrderDao();
        OrderItemDao oiDao = new OrderItemDao();

        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        String detailIdParam = request.getParameter("detailId");

        if (keyword != null) {
            keyword = keyword.trim();
        } else {
            keyword = "";
        }

        if (status != null) {
            status = status.trim();
        }

        if (status != null && status.isBlank()) {
            status = null;
        }

        if (status != null && !VALID_STATUSES.contains(status)) {
            status = null;
        }

        List<Order> orders = orderDao.getAdminOrders(keyword, status);

        Order selectedOrder = null;
        List<OrderItem> selectedOrderItems = new ArrayList<>();

        if (detailIdParam != null && !detailIdParam.isBlank()) {
            try {
                int detailId = Integer.parseInt(detailIdParam);
                selectedOrder = orderDao.getOrderById(detailId);

                if (selectedOrder != null) {
                    selectedOrderItems = oiDao.getItemsByOrderId(detailId);
                }
            } catch (NumberFormatException ignored) {
            }
        }

        request.setAttribute("orders", orders);
        request.setAttribute("selectedOrder", selectedOrder);
        request.setAttribute("selectedOrderItems", selectedOrderItems);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentStatus", status);
        request.setAttribute("allCount", orderDao.countOrders());
        request.setAttribute("pendingCount", orderDao.countOrdersByStatus("PENDING"));
        request.setAttribute("confirmedCount", orderDao.countOrdersByStatus("CONFIRMED"));
        request.setAttribute("shippedCount", orderDao.countOrdersByStatus("SHIPPED"));
        request.setAttribute("completedCount", orderDao.countOrdersByStatus("COMPLETED"));
        request.setAttribute("cancelledCount", orderDao.countOrdersByStatus("CANCELLED"));

        request.setAttribute("notificationCount", orderDao.countAdminNotifications());
        request.setAttribute("latestNotifications", orderDao.getLatestAdminNotifications(5));

        request.getRequestDispatcher("/jsp/adminjsp/Admin_DonHang.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao orderDao = new OrderDao();

        String action = request.getParameter("action");

        if ("updateStatus".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String status = request.getParameter("status");

                if (status != null) {
                    status = status.trim();
                }

                if (status != null && VALID_STATUSES.contains(status)) {
                    orderDao.updateStatus(orderId, status);
                }
            } catch (NumberFormatException ignored) {
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}