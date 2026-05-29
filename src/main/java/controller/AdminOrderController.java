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

@WebServlet(name = "AdminOrderController", value = "/admin/orders")
public class AdminOrderController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao orderDao = new OrderDao();
        OrderItemDao oiDao = new OrderItemDao();

        String status = request.getParameter("status");
        String detailIdParam = request.getParameter("detailId");

        List<Order> orders;

        if (status != null && !status.isBlank()) {
            orders = orderDao.getOrdersByStatus(status);
        } else {
            orders = orderDao.getAllOrders();
        }

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

                if (status != null && !status.isBlank()) {
                    orderDao.updateStatus(orderId, status);
                }
            } catch (NumberFormatException ignored) {
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}