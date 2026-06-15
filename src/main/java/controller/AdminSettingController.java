package controller;

import dao.OrderDao;
import dao.PermissionDao;
import dao.StoreSettingDao;
import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import model.StoreSetting;
import util.PasswordUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        StoreSettingDao storeSettingDao = new StoreSettingDao();

        String adminKeyword = request.getParameter("adminKeyword");
        adminKeyword = adminKeyword == null ? "" : adminKeyword.trim();

        boolean canManageSetting = canManageSetting(request);

        List<User> admins = Collections.emptyList();
        Map<Integer, String> adminPermissionMap = Collections.emptyMap();

        if (canManageSetting) {
            admins = uDao.searchAdminAccounts(adminKeyword);

            adminPermissionMap = admins.stream()
                    .collect(Collectors.toMap(
                            User::getUserId,
                            admin -> String.join(",", pDao.getPermissionCodesByUserId(admin.getUserId())),
                            (oldValue, newValue) -> oldValue
                    ));

            request.setAttribute("permissions", pDao.getAllPermissions());
        }

        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("latestNotifications", oDao.getLatestAdminNotifications(20));
        request.setAttribute("admins", admins);
        request.setAttribute("adminPermissionMap", adminPermissionMap);
        request.setAttribute("adminKeyword", adminKeyword);
        request.setAttribute("canManageSetting", canManageSetting);
        StoreSetting storeSetting = storeSettingDao.getStoreSetting();
        request.setAttribute("storeSetting", storeSetting);
        getServletContext().setAttribute("storeSetting", storeSetting);

        request.getRequestDispatcher("/jsp/adminjsp/Admin_CaiDat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String adminKeyword = request.getParameter("adminKeyword");
        adminKeyword = adminKeyword == null ? "" : adminKeyword.trim();

        if ("updatePermission".equals(action)) {
            if (!canManageSetting(request)) {
                request.getSession().setAttribute("settingMessage", "Bạn không có quyền phân quyền cho tài khoản khác");
            } else {
                updatePermission(request);
                request.getSession().setAttribute("settingMessage", "Lưu phân quyền thành công");
            }
        } else if ("updateStore".equals(action)) {
            if (!canManageSetting(request)) {
                request.getSession().setAttribute("settingMessage", "Bạn không có quyền cập nhật thông tin cửa hàng");
            } else {
                updateStore(request);
            }
        } else if ("changePassword".equals(action)) {
            changePassword(request);
        } else {
            request.getSession().setAttribute("settingMessage", "Không tìm thấy hành động cài đặt");
        }

        String redirectUrl = request.getContextPath() + "/admin/setting";

        if (!adminKeyword.isBlank()) {
            redirectUrl += "?adminKeyword=" + URLEncoder.encode(adminKeyword, StandardCharsets.UTF_8);
        }

        response.sendRedirect(redirectUrl);
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

    private void updateStore(HttpServletRequest request) {
        StoreSetting setting = new StoreSetting();
        setting.setStoreName(clean(request.getParameter("storeName")));
        setting.setStoreEmail(clean(request.getParameter("storeEmail")));
        setting.setStorePhone(clean(request.getParameter("storePhone")));
        setting.setStoreWebsite(clean(request.getParameter("storeWebsite")));
        setting.setStoreAddress(clean(request.getParameter("storeAddress")));

        if (setting.getStoreName().length() < 2
                || (!setting.getStoreEmail().isBlank()
                    && !setting.getStoreEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
                || (!setting.getStorePhone().isBlank()
                    && !setting.getStorePhone().matches("^[0-9+ .()-]{8,20}$"))) {
            request.getSession().setAttribute("settingMessage", "Thông tin cửa hàng chưa hợp lệ.");
            return;
        }

        StoreSettingDao storeSettingDao = new StoreSettingDao();
        if (storeSettingDao.save(setting)) {
            getServletContext().setAttribute("storeSetting", setting);
            request.getSession().setAttribute("settingMessage", "Lưu thông tin cửa hàng thành công");
        } else {
            request.getSession().setAttribute("settingMessage", "Không thể lưu thông tin cửa hàng");
        }
    }

    private void changePassword(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        if (currentUser.getGoogleId() != null) {
            session.setAttribute("settingMessage", "Tài khoản đăng nhập bằng Google không thể đổi mật khẩu tại đây");
            return;
        }

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (currentPassword == null || currentPassword.isBlank()
                || newPassword == null || newPassword.isBlank()
                || confirmPassword == null || confirmPassword.isBlank()) {
            session.setAttribute("settingMessage", "Vui lòng nhập đầy đủ thông tin mật khẩu");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("settingMessage", "Mật khẩu xác nhận không khớp");
            return;
        }

        boolean strong =
                newPassword.length() >= 8
                        && newPassword.matches(".*[A-Z].*")
                        && newPassword.matches(".*[a-z].*")
                        && newPassword.matches(".*\\d.*")
                        && newPassword.matches(".*[^A-Za-z0-9].*");

        if (!strong) {
            session.setAttribute("settingMessage", "Mật khẩu mới phải có ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
            return;
        }

        if (!PasswordUtil.verify(currentPassword, currentUser.getPassword())) {
            session.setAttribute("settingMessage", "Mật khẩu hiện tại không đúng");
            return;
        }

        String hashedPassword = PasswordUtil.hash(newPassword);

        UserDao uDao = new UserDao();
        boolean updated = uDao.updatePassword(currentUser.getUserId(), hashedPassword);

        if (!updated) {
            session.setAttribute("settingMessage", "Đổi mật khẩu thất bại");
            return;
        }

        currentUser.setPassword(hashedPassword);
        session.setAttribute("user", currentUser);
        session.setAttribute("settingMessage", "Đổi mật khẩu thành công");
    }

    private boolean canManageSetting(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        Object permissionTextObj = session.getAttribute("permissionCodesText");

        if (permissionTextObj == null) {
            return false;
        }

        String permissionCodesText = permissionTextObj.toString();

        return permissionCodesText.contains(",MANAGE_SETTING,");
    }

    private String clean(String value) {
        return value == null ? "" : value.trim().replaceAll("\\s+", " ");
    }
}
