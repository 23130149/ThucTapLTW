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
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/SignIn");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (!"ADMIN".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/jsp/home.jsp");
            return;
        }

        PermissionDao pDao = new PermissionDao();
        Set<String> permissions = pDao.getPermissionCodesByUserId(user.getUserId());

        session.setAttribute("permissions", permissions);
        session.setAttribute("permissionCodesText", "," + String.join(",", permissions) + ",");

        String requiredPermission = getRequiredPermission(req.getRequestURI(), req.getContextPath());

        if (requiredPermission != null && isModifyRequest(req)) {
            if (!permissions.contains(requiredPermission)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thực hiện chức năng này");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isModifyRequest(HttpServletRequest req) {
        String method = req.getMethod();

        if ("POST".equalsIgnoreCase(method)
                || "PUT".equalsIgnoreCase(method)
                || "DELETE".equalsIgnoreCase(method)) {
            return true;
        }

        String action = req.getParameter("action");

        if (action == null || action.isBlank()) {
            return false;
        }

        return action.equalsIgnoreCase("add")
                || action.equalsIgnoreCase("create")
                || action.equalsIgnoreCase("insert")
                || action.equalsIgnoreCase("edit")
                || action.equalsIgnoreCase("update")
                || action.equalsIgnoreCase("delete")
                || action.equalsIgnoreCase("remove")
                || action.equalsIgnoreCase("save")
                || action.equalsIgnoreCase("reply")
                || action.equalsIgnoreCase("approve")
                || action.equalsIgnoreCase("reject")
                || action.equalsIgnoreCase("confirm")
                || action.equalsIgnoreCase("cancel")
                || action.equalsIgnoreCase("hide")
                || action.equalsIgnoreCase("changeStatus")
                || action.equalsIgnoreCase("updateStatus");
    }

    private String getRequiredPermission(String requestUri, String contextPath) {
        String path = requestUri.substring(contextPath.length());

        if (path.startsWith("/admin/category")) return "MANAGE_CATEGORY";
        if (path.startsWith("/admin/products")) return "MANAGE_PRODUCT";
        if (path.startsWith("/admin/orders")) return "MANAGE_ORDER";
        if (path.startsWith("/admin/customers")) return "MANAGE_CUSTOMER";
        if (path.startsWith("/admin/reviews")) return "MANAGE_REVIEW";
        if (path.startsWith("/admin/contact")) return "MANAGE_CONTACT";
        if (path.startsWith("/admin/contacts")) return "MANAGE_CONTACT";
        if (path.startsWith("/admin/banner")) return "MANAGE_BANNER";
        if (path.startsWith("/admin/setting")) return "MANAGE_SETTING";

        return null;
    }
}