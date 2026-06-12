package controller;

import dao.OrderDao;
import dao.PermissionDao;
import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "AdminSettingController", value = "/admin/setting")
public class AdminSettingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao oDao = new OrderDao();
        UserDao uDao = new UserDao();
        PermissionDao pDao = new PermissionDao();

        List<User> admins = uDao.getAllAdminAccounts();

        Map<Integer, String> adminPermissionMap = admins.stream()
                .collect(Collectors.toMap(
                        User::getUserId,
                        admin -> String.join(",", pDao.getPermissionCodesByUserId(admin.getUserId())),
                        (oldValue, newValue) -> oldValue
                ));
        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("admins", admins);
        request.setAttribute("permissions", pDao.getAllPermissions());
        request.setAttribute("adminPermissionMap", adminPermissionMap);


        request.getRequestDispatcher("/jsp/adminjsp/Admin_CaiDat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("updatePermission".equals(action)) {
            updatePermission(request);
            request.getSession().setAttribute("settingMessage", "Lưu phân quyền thành công");
        } else if ("updateStore".equals(action)) {
            request.getSession().setAttribute("settingMessage", "Lưu thông tin cửa hàng thành công");
        } else if ("changePassword".equals(action)) {
            request.getSession().setAttribute("settingMessage", "Chức năng đổi mật khẩu sẽ được xử lý ở phiên bản sau");
        } else {
            request.getSession().setAttribute("settingMessage", "Không tìm thấy hành động cài đặt");
        }

        response.sendRedirect(request.getContextPath() + "/admin/setting");
    }

    private void updatePermission(HttpServletRequest request) {
        String adminIdRaw = request.getParameter("adminId");

        if (adminIdRaw == null || adminIdRaw.isBlank()) {
            return;
        }

        int adminId;

        try {
            adminId = Integer.parseInt(adminIdRaw);
        } catch (NumberFormatException e) {
            return;
        }

        String[] selectedPermissions = request.getParameterValues("permissions");

        List<String> permissionCodes = selectedPermissions == null
                ? Collections.emptyList()
                : Arrays.asList(selectedPermissions);

        PermissionDao pDao = new PermissionDao();
        pDao.updateUserPermissions(adminId, permissionCodes);
    }
}