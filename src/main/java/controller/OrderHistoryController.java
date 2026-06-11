package controller;

import dao.OrderDao;
import model.Order;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/OrderHistory")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class OrderHistoryController extends HttpServlet {

    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        User user = (User) session.getAttribute("user");

        int page = parsePositiveInt(request.getParameter("page"), 1);
        int pageSize = 8;
        String activeStatus = normalizeStatus(request.getParameter("status"));
        List<String> statuses = toStatuses(activeStatus);

        int totalOrders = orderDao.countOrdersByUserIdAndStatuses(user.getUserId(), statuses);
        int totalPages = (int) Math.ceil(totalOrders * 1.0 / pageSize);
        if (totalPages == 0) {
            totalPages = 1;
        }
        if (page > totalPages) {
            page = totalPages;
        }

        int offset = (page - 1) * pageSize;
        List<Order> orderList = orderDao.getOrdersByUserIdAndStatusesPaged(
                user.getUserId(), statuses, pageSize, offset
        );
        Map<String, Integer> statusCounts = orderDao.countOrderStatusGroupsByUser(user.getUserId());

        request.setAttribute("orderList", orderList != null ? orderList : new ArrayList<>());
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("activeStatus", activeStatus);
        request.setAttribute("statusCounts", statusCounts);

        request.getRequestDispatcher("/jsp/orderhistory.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        int orderId = parsePositiveInt(request.getParameter("orderId"), 0);
        String action = request.getParameter("action");
        String reason = request.getParameter("reason");
        boolean success = false;

        if (orderId <= 0) {
            session.setAttribute("orderMessage", "Không tìm thấy đơn hàng cần xử lý.");
            response.sendRedirect(request.getContextPath() + "/OrderHistory");
            return;
        }

        if ("cancel".equals(action)) {
            success = orderDao.cancelOrderByUser(orderId, user.getUserId(), reason);
            session.setAttribute("orderMessage", success
                    ? "Đã huỷ đơn hàng và khôi phục số lượng sản phẩm trong kho."
                    : "Không thể huỷ đơn. Vui lòng nhập lý do hoặc kiểm tra trạng thái đơn hàng.");
        } else if ("return".equals(action)) {
            String imagePath = saveReturnImageIfPresent(request);
            success = orderDao.requestReturnByUser(orderId, user.getUserId(), reason, imagePath);
            session.setAttribute("orderMessage", success
                    ? "Đã gửi yêu cầu trả hàng. Cửa hàng sẽ kiểm tra và phản hồi sớm."
                    : "Không thể gửi yêu cầu trả hàng. Chỉ đơn đã hoàn thành mới được trả hàng và cần có lý do.");
        } else if ("confirmReceived".equals(action)) {
            success = orderDao.confirmReceivedByUser(orderId, user.getUserId());
            session.setAttribute("orderMessage", success
                    ? "Đã xác nhận nhận hàng. Đơn hàng đã hoàn thành."
                    : "Chưa thể xác nhận nhận hàng vì GHN chưa báo đã giao.");
        }

        String redirect = request.getHeader("Referer");
        if (redirect == null || redirect.isBlank()) {
            redirect = request.getContextPath() + "/OrderHistory";
        }
        response.sendRedirect(redirect);
    }

    private int parsePositiveInt(String raw, int defaultValue) {
        try {
            int value = Integer.parseInt(raw);
            return value > 0 ? value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private String normalizeStatus(String raw) {
        if (raw == null || raw.isBlank()) {
            return "all";
        }
        return switch (raw.trim().toLowerCase()) {
            case "processing", "shipping", "completed", "cancelled", "returned" -> raw.trim().toLowerCase();
            default -> "all";
        };
    }

    private List<String> toStatuses(String activeStatus) {
        return switch (activeStatus) {
            case "processing" -> Arrays.asList("PENDING", "PROCESSING", "CONFIRMED");
            case "shipping" -> Arrays.asList("SHIPPED", "DELIVERED");
            case "completed" -> Collections.singletonList("COMPLETED");
            case "cancelled" -> Collections.singletonList("CANCELLED");
            case "returned" -> Arrays.asList("RETURN_REQUESTED", "RETURNED", "RETURN_REJECTED");
            default -> Collections.emptyList();
        };
    }

    private String saveReturnImageIfPresent(HttpServletRequest request) {
        try {
            Part part = request.getPart("returnImage");
            if (part == null || part.getSize() == 0) {
                return "";
            }

            String contentType = part.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return "";
            }

            String submittedName = part.getSubmittedFileName();
            String extension = ".jpg";
            if (submittedName != null && submittedName.contains(".")) {
                extension = submittedName.substring(submittedName.lastIndexOf('.'));
            }

            String uploadDir = getServletContext().getRealPath("/uploads/returns");
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = UUID.randomUUID() + extension;
            part.write(new File(dir, fileName).getAbsolutePath());
            return "/uploads/returns/" + fileName;
        } catch (Exception ignored) {
            return "";
        }
    }
}
