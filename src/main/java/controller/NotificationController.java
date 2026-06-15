package controller;

import dao.NotificationDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.AjaxUtil;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "NotificationController", value = {"/notifications", "/notifications/read", "/notifications/go"})
public class NotificationController extends HttpServlet {
    private final NotificationDao notificationDao = new NotificationDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getCurrentUser(request);
        String path = request.getServletPath();

        if (user == null) {
            if (AjaxUtil.wantsJson(request) || "/notifications".equals(path)) {
                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("success", false);
                payload.put("loggedIn", false);
                payload.put("unreadCount", 0);
                payload.put("notifications", java.util.List.of());
                payload.put("message", "Vui lòng đăng nhập để xem thông báo.");
                AjaxUtil.writeJson(response, payload);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        if ("/notifications/go".equals(path)) {
            int id = parseInt(request.getParameter("id"), 0);
            String target = notificationDao.findTargetUrl(id, user.getUserId());
            notificationDao.markRead(id, user.getUserId());
            response.sendRedirect(safeTarget(request, target));
            return;
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        try {
            payload.put("success", true);
            payload.put("loggedIn", true);
            payload.put("unreadCount", notificationDao.countUnread(user.getUserId()));
            payload.put("notifications", notificationDao.getLatest(user.getUserId(), 12));
        } catch (Exception e) {
            payload.put("success", false);
            payload.put("loggedIn", true);
            payload.put("unreadCount", 0);
            payload.put("notifications", java.util.List.of());
            payload.put("message", "Không đọc được dữ liệu thông báo: " + e.getClass().getSimpleName());
        }
        AjaxUtil.writeJson(response, payload);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            AjaxUtil.writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, AjaxUtil.error("Vui lòng đăng nhập để xem thông báo."));
            return;
        }

        String action = request.getParameter("action");
        boolean success;
        if ("markAllRead".equals(action)) {
            notificationDao.markAllRead(user.getUserId());
            success = true;
        } else {
            int id = parseInt(request.getParameter("id"), 0);
            success = notificationDao.markRead(id, user.getUserId());
        }

        Map<String, Object> payload = success ? AjaxUtil.ok("Đã cập nhật thông báo.") : AjaxUtil.error("Không tìm thấy thông báo.");
        payload.put("unreadCount", notificationDao.countUnread(user.getUserId()));
        AjaxUtil.writeJson(response, payload);
    }

    private User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (User) session.getAttribute("user");
    }

    private int parseInt(String raw, int fallback) {
        try {
            return Integer.parseInt(raw);
        } catch (Exception e) {
            return fallback;
        }
    }

    private String safeTarget(HttpServletRequest request, String target) {
        String contextPath = request.getContextPath();
        if (target == null || target.isBlank()) {
            return contextPath + "/Account";
        }
        String trimmed = target.trim();
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("//")) {
            return contextPath + "/Account";
        }
        if (!trimmed.startsWith("/")) {
            trimmed = "/" + trimmed;
        }
        return contextPath + trimmed;
    }
}
