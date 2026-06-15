package controller;

import dao.NotificationDao;
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
import util.AjaxUtil;
import util.FormatUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            "DELIVERED",
            "COMPLETED",
            "CANCELLED",
            "RETURN_REQUESTED",
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
        request.setAttribute("deliveredCount", oDao.countOrdersByStatus("DELIVERED"));
        request.setAttribute("completedCount", oDao.countOrdersByStatus("COMPLETED"));
        request.setAttribute("cancelledCount", oDao.countOrdersByStatus("CANCELLED"));
        request.setAttribute("returnRequestedCount", oDao.countOrdersByStatus("RETURN_REQUESTED"));
        request.setAttribute("returnedCount", oDao.countOrdersByStatus("RETURNED"));
        request.setAttribute("returnRejectedCount", oDao.countOrdersByStatus("RETURN_REJECTED"));

        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("latestNotifications", oDao.getLatestAdminNotifications(20));
        request.setAttribute("ghnSimulation", new GhnService().isSimulation());

        request.getRequestDispatcher("/jsp/adminjsp/Admin_DonHang.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        OrderDao orderDao = new OrderDao();
        NotificationDao notificationDao = new NotificationDao();
        OrderItemDao orderItemDao = new OrderItemDao();
        UserAddressDao userAddressDao = new UserAddressDao();
        GhnService ghnService = new GhnService();

        String action = request.getParameter("action");
        String message = null;
        boolean success = false;

        if ("updateStatus".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String status = request.getParameter("status");

                if (status != null) {
                    status = status.trim();
                }

                if (status != null && Set.of("PROCESSING", "CONFIRMED", "RETURNED", "RETURN_REJECTED").contains(status)) {
                    success = orderDao.updateStatus(orderId, status);
                    if (success) {
                        Order updatedOrder = orderDao.getOrderById(orderId);
                        notifyOrder(notificationDao, updatedOrder, "ORDER_STATUS",
                                "Đơn hàng của bạn đã cập nhật trạng thái",
                                "Trạng thái mới: " + FormatUtil.orderStatusLabel(status));
                    } else {
                        message = "Không tìm thấy đơn hàng cần cập nhật.";
                    }
                } else {
                    message = "Trạng thái đơn hàng không hợp lệ.";
                }
            } catch (NumberFormatException ignored) {
                message = "Mã đơn hàng không hợp lệ.";
            }
        } else if ("createGhn".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                Order order = orderDao.getOrderById(orderId);
                if (order == null || !"CONFIRMED".equals(order.getStatus())) {
                    message = "Chỉ đơn đã xác nhận mới được gửi sang GHN.";
                } else if (order.getGhnOrderCode() != null && !order.getGhnOrderCode().isBlank()) {
                    message = "Đơn hàng đã có vận đơn GHN.";
                } else {
                    UserAddress address = userAddressDao.findById(order.getUserAddressId());
                    List<OrderItem> items = orderItemDao.getItemsByOrderId(orderId);
                    GhnService.GhnOrderResult result = ghnService.createOrder(order, address, items);
                    if (!orderDao.saveGhnShipping(orderId, result)) {
                        message = "Không thể lưu vận đơn GHN vào đơn hàng.";
                    } else {
                        success = true;
                        Order updatedOrder = orderDao.getOrderById(orderId);
                        notifyOrder(notificationDao, updatedOrder, "ORDER_SHIPPING",
                                "Đơn hàng của bạn đã có vận đơn",
                                "Mã vận đơn GHN: " + result.getOrderCode());
                    }
                }
            } catch (NumberFormatException e) {
                message = "Mã đơn hàng không hợp lệ.";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                message = "Kết nối GHN bị gián đoạn.";
            } catch (Exception e) {
                message = "Không thể tạo vận đơn GHN: " + e.getMessage();
            }
        } else if ("syncGhn".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                Order order = orderDao.getOrderById(orderId);
                if (order == null || order.getGhnOrderCode() == null || order.getGhnOrderCode().isBlank()) {
                    message = "Đơn hàng chưa có vận đơn GHN.";
                } else {
                    GhnService.GhnOrderResult result = ghnService.getOrderDetail(
                            order.getGhnOrderCode(), order.getGhnStatus()
                    );
                    success = orderDao.updateGhnStatus(orderId, result);
                    if (success) {
                        Order updatedOrder = orderDao.getOrderById(orderId);
                        notifyOrder(notificationDao, updatedOrder, "ORDER_SHIPPING",
                                "Trạng thái giao hàng đã cập nhật",
                                "GHN: " + FormatUtil.ghnStatusLabel(result.getStatus()));
                    } else {
                        message = "Không thể cập nhật trạng thái GHN cho đơn hàng.";
                    }
                }
            } catch (NumberFormatException e) {
                message = "Mã đơn hàng không hợp lệ.";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                message = "Kết nối GHN bị gián đoạn.";
            } catch (Exception e) {
                message = "Không thể cập nhật trạng thái GHN: " + e.getMessage();
            }
        } else if ("markPaymentPaid".equals(action) || "markCashPaid".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                if (!orderDao.markManualPaymentPaid(orderId)) {
                    message = "Không thể xác nhận thanh toán cho đơn hàng này.";
                } else {
                    success = true;
                    Order updatedOrder = orderDao.getOrderById(orderId);
                    notifyOrder(notificationDao, updatedOrder, "ORDER_PAYMENT",
                            "Thanh toán đơn hàng đã được xác nhận",
                            "Đơn hàng " + orderCode(updatedOrder) + " đã được ghi nhận thanh toán.");
                }
            } catch (NumberFormatException e) {
                message = "Mã đơn hàng không hợp lệ.";
            }
        } else {
            message = "Thao tác đơn hàng không hợp lệ.";
        }

        if (!success && message != null) {
            request.getSession().setAttribute("adminOrderMessage", message);
        }
        if (AjaxUtil.wantsJson(request)) {
            Map<String, Object> payload = success
                    ? AjaxUtil.ok("Đã cập nhật đơn hàng.")
                    : AjaxUtil.error(message == null ? "Không thể cập nhật đơn hàng." : message);
            String orderId = request.getParameter("orderId");
            if (orderId != null) {
                payload.put("orderId", orderId);
            }
            payload.put("action", action);
            AjaxUtil.writeJson(response, payload);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
    private void notifyOrder(NotificationDao notificationDao, Order order, String type, String title, String message) {
        if (order == null || order.getUserId() <= 0) {
            return;
        }
        notificationDao.addOrRefreshSafe(
                order.getUserId(),
                type,
                title,
                message,
                "/OrderDetail?orderId=" + order.getOrderId(),
                "ORDER",
                order.getOrderId()
        );
    }

    private String orderCode(Order order) {
        if (order == null || order.getOrderCode() == null || order.getOrderCode().isBlank()) {
            return "của bạn";
        }
        return order.getOrderCode();
    }

}
