package filter;

import dao.PermissionDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;
import java.util.Set;

@WebFilter({"/admin/*", "/jsp/adminjsp/*"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/SignIn");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (user.getRole() == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        PermissionDao pDao = new PermissionDao();
        Set<String> permissions = pDao.getPermissionCodesByUserId(user.getUserId());

        if (permissions == null) {
            permissions = Set.of();
        }

        session.setAttribute("permissions", permissions);
        session.setAttribute("permissionCodesText", "," + String.join(",", permissions) + ",");

        String path = getPath(req);
        String requiredPermission = getRequiredPermission(path);

        if (requiredPermission != null && !permissions.contains(requiredPermission)) {
            request.setAttribute("accessDenied", true);
            request.setAttribute("accessDeniedMessage", "Bạn không có quyền quản lý trang này");

            String jspPage = getJspPage(path);

            if (jspPage != null) {
                request.getRequestDispatcher(jspPage).forward(request, response);
                return;
            }

            request.getRequestDispatcher("/jsp/adminjsp/Admin_TongQuan.jsp").forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    private String getRequiredPermission(String path) {
        if (path.startsWith("/admin/category")
                || path.startsWith("/jsp/adminjsp/Admin_DanhMuc.jsp")) {
            return "MANAGE_CATEGORY";
        }

        if (path.startsWith("/admin/products")
                || path.startsWith("/jsp/adminjsp/Admin_SanPham.jsp")) {
            return "MANAGE_PRODUCT";
        }

        if (path.startsWith("/admin/orders")
                || path.startsWith("/jsp/adminjsp/Admin_DonHang.jsp")) {
            return "MANAGE_ORDER";
        }

        if (path.startsWith("/admin/customers")
                || path.startsWith("/jsp/adminjsp/Admin_KhachHang.jsp")) {
            return "MANAGE_CUSTOMER";
        }

        if (path.startsWith("/admin/reviews")
                || path.startsWith("/jsp/adminjsp/Admin_DanhGia.jsp")) {
            return "MANAGE_REVIEW";
        }

        if (path.startsWith("/admin/contact")
                || path.startsWith("/admin/contacts")
                || path.startsWith("/jsp/adminjsp/Admin_LienHe.jsp")) {
            return "MANAGE_CONTACT";
        }

        if (path.startsWith("/admin/banner")
                || path.startsWith("/jsp/adminjsp/Admin_Banner.jsp")) {
            return "MANAGE_BANNER";
        }

        return null;
    }
    private String getJspPage(String path) {
        if (path.startsWith("/admin/category")
                || path.startsWith("/jsp/adminjsp/Admin_DanhMuc.jsp")) {
            return "/jsp/adminjsp/Admin_DanhMuc.jsp";
        }

        if (path.startsWith("/admin/products")
                || path.startsWith("/jsp/adminjsp/Admin_SanPham.jsp")) {
            return "/jsp/adminjsp/Admin_SanPham.jsp";
        }

        if (path.startsWith("/admin/orders")
                || path.startsWith("/jsp/adminjsp/Admin_DonHang.jsp")) {
            return "/jsp/adminjsp/Admin_DonHang.jsp";
        }

        if (path.startsWith("/admin/customers")
                || path.startsWith("/jsp/adminjsp/Admin_KhachHang.jsp")) {
            return "/jsp/adminjsp/Admin_KhachHang.jsp";
        }

        if (path.startsWith("/admin/reviews")
                || path.startsWith("/jsp/adminjsp/Admin_DanhGia.jsp")) {
            return "/jsp/adminjsp/Admin_DanhGia.jsp";
        }

        if (path.startsWith("/admin/contact")
                || path.startsWith("/admin/contacts")
                || path.startsWith("/jsp/adminjsp/Admin_LienHe.jsp")) {
            return "/jsp/adminjsp/Admin_LienHe.jsp";
        }

        if (path.startsWith("/admin/banner")
                || path.startsWith("/jsp/adminjsp/Admin_Banner.jsp")) {
            return "/jsp/adminjsp/Admin_Banner.jsp";
        }

        if (path.startsWith("/admin/setting")
                || path.startsWith("/jsp/adminjsp/Admin_CaiDat.jsp")) {
            return "/jsp/adminjsp/Admin_CaiDat.jsp";
        }

        return null;
    }
}