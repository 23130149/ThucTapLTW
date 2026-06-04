package controller;

import dao.OrderDao;
import dao.OrderItemDao;
import dao.UserAddressDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Order;
import model.OrderItem;
import model.UserAddress;
import service.GhnService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "AdminOrderController", value = "/admin/orders")
public class AdminOrderController extends HttpServlet {
    private static final Set<String> VALID_STATUSES = Set.of(
            "PENDING",
            "PENDING_PAYMENT",
            "PAYMENT_FAILED",
            "PROCESSING",
            "CONFIRMED",
            "SHIPPED",
            "COMPLETED",
            "CANCELLED",
            "RETURN_REQUESTED",
            "RETURNED",
            "RETURN_REJECTED"
    );
    private static final Set<String> MANUAL_STATUSES = Set.of(
            "PROCESSING",
            "CONFIRMED",
            "RETURNED",
            "RETURN_REJECTED"
    );
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao oDao = new OrderDao();
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

        List<Order> orders = oDao.getAdminOrders(keyword, status);

        Order selectedOrder = null;
        List<OrderItem> selectedOrderItems = new ArrayList<>();

        if (detailIdParam != null && !detailIdParam.isBlank()) {
            try {
                int detailId = Integer.parseInt(detailIdParam);
                selectedOrder = oDao.getOrderById(detailId);

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
        request.setAttribute("allCount", oDao.countOrders());
        request.setAttribute("pendingCount", oDao.countOrdersByStatus("PENDING"));
        request.setAttribute("pendingPaymentCount", oDao.countOrdersByStatus("PENDING_PAYMENT"));
        request.setAttribute("paymentFailedCount", oDao.countOrdersByStatus("PAYMENT_FAILED"));
        request.setAttribute("processingCount", oDao.countOrdersByStatus("PROCESSING"));
        request.setAttribute("confirmedCount", oDao.countOrdersByStatus("CONFIRMED"));
        request.setAttribute("shippedCount", oDao.countOrdersByStatus("SHIPPED"));
        request.setAttribute("completedCount", oDao.countOrdersByStatus("COMPLETED"));
        request.setAttribute("cancelledCount", oDao.countOrdersByStatus("CANCELLED"));
        request.setAttribute("returnRequestedCount", oDao.countOrdersByStatus("RETURN_REQUESTED"));
        request.setAttribute("returnedCount", oDao.countOrdersByStatus("RETURNED"));
        request.setAttribute("returnRejectedCount", oDao.countOrdersByStatus("RETURN_REJECTED"));

        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("latestNotifications", oDao.getLatestAdminNotifications(5));

        request.getRequestDispatcher("/jsp/adminjsp/Admin_DonHang.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao orderDao = new OrderDao();
        HttpSession session = request.getSession();

        String action = request.getParameter("action");

        if ("updateStatus".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String status = request.getParameter("status");

                if (status != null) {
                    status = status.trim();
                }

                if ("SHIPPED".equals(status)) {
                    createGhnOrder(orderId, orderDao, session);
                } else if (status != null && MANUAL_STATUSES.contains(status)) {
                    orderDao.updateStatus(orderId, status);
                }
            } catch (NumberFormatException ignored) {
            }
        } else if ("syncGhn".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                syncGhnOrder(orderId, orderDao, session);
            } catch (NumberFormatException ignored) {
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }

    private void createGhnOrder(int orderId, OrderDao orderDao, HttpSession session) {
        Order order = orderDao.getOrderById(orderId);
        if (order == null || !"CONFIRMED".equals(order.getStatus())) {
            session.setAttribute("adminOrderMessage", "Đơn hàng chưa sẵn sàng để giao cho GHN.");
            return;
        }

        if (order.getGhnOrderCode() != null && !order.getGhnOrderCode().isBlank()) {
            orderDao.updateStatus(orderId, "SHIPPED");
            session.setAttribute("adminOrderMessage", "Đơn hàng đã có vận đơn GHN.");
            return;
        }

        UserAddress address = new UserAddressDao().findById(order.getUserAddressId());
        List<OrderItem> items = new OrderItemDao().getItemsByOrderId(orderId);

        try {
            GhnService.GhnOrderResult result = new GhnService().createOrder(order, address, items);
            orderDao.saveGhnShipping(orderId, result.getOrderCode(), result.getStatus(), result.getLeadtime(), result.getFinishDate());
            session.setAttribute("adminOrderMessage", "Đã tạo vận đơn GHN " + result.getOrderCode() + ".");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            session.setAttribute("adminOrderMessage", "Không thể tạo vận đơn GHN lúc này.");
        } catch (IOException e) {
            session.setAttribute("adminOrderMessage", "GHN chưa nhận vận đơn: " + e.getMessage());
        }
    }

    private void syncGhnOrder(int orderId, OrderDao orderDao, HttpSession session) {
        Order order = orderDao.getOrderById(orderId);
        if (order == null || order.getGhnOrderCode() == null || order.getGhnOrderCode().isBlank()) {
            session.setAttribute("adminOrderMessage", "Đơn hàng chưa có mã vận đơn GHN.");
            return;
        }

        try {
            GhnService.GhnOrderResult result = new GhnService().getOrderDetail(order.getGhnOrderCode());
            orderDao.updateGhnStatus(orderId, result.getStatus(), result.getLeadtime(), result.getFinishDate());
            session.setAttribute("adminOrderMessage", "Đã cập nhật trạng thái GHN.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            session.setAttribute("adminOrderMessage", "Không thể cập nhật GHN lúc này.");
        } catch (IOException e) {
            session.setAttribute("adminOrderMessage", "Không thể cập nhật GHN: " + e.getMessage());
        }
    }
}
