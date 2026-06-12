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
        OrderItemDao orderItemDao = new OrderItemDao();
        UserAddressDao userAddressDao = new UserAddressDao();
        GhnService ghnService = new GhnService();

        String action = request.getParameter("action");
        String message = null;

        if ("updateStatus".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String status = request.getParameter("status");

                if (status != null) {
                    status = status.trim();
                }

                if (status != null && Set.of("PROCESSING", "CONFIRMED", "RETURNED", "RETURN_REJECTED").contains(status)) {
                    orderDao.updateStatus(orderId, status);
                }
            } catch (NumberFormatException ignored) {
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
                    orderDao.updateGhnStatus(orderId, result);
                }
            } catch (NumberFormatException e) {
                message = "Mã đơn hàng không hợp lệ.";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                message = "Kết nối GHN bị gián đoạn.";
            } catch (Exception e) {
                message = "Không thể cập nhật trạng thái GHN: " + e.getMessage();
            }
        } else if ("markCashPaid".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                if (!orderDao.markCashPaid(orderId)) {
                    message = "Không thể xác nhận thanh toán cho đơn hàng này.";
                }
            } catch (NumberFormatException e) {
                message = "Mã đơn hàng không hợp lệ.";
            }
        }

        if (message != null) {
            request.getSession().setAttribute("adminOrderMessage", message);
        }
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}
