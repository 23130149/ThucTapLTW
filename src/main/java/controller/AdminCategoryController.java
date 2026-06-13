package controller;

import dao.CategoryDao;
import dao.OrderDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Category;
import util.AjaxUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminCategoryController", value = "/admin/category")
public class AdminCategoryController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryDao cDao = new CategoryDao();
        OrderDao oDao = new OrderDao();

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int categoryId = Integer.parseInt(request.getParameter("id"));
            boolean success = cDao.deleteCategory(categoryId);
            if (AjaxUtil.wantsJson(request)) {
                Map<String, Object> payload = success
                        ? AjaxUtil.ok("Đã xóa danh mục.")
                        : AjaxUtil.error("Không tìm thấy danh mục cần xóa.");
                payload.put("action", action);
                payload.put("categoryId", categoryId);
                AjaxUtil.writeJson(response, payload);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin/category");
            return;
        }

        String keyword = request.getParameter("keyword");
        List<Category> list;

        if (keyword != null && !keyword.trim().isEmpty()) {
            list = cDao.searchCategories(keyword.trim());
        } else {
            list = cDao.getAllCategories();
        }

        int totalCategory = cDao.getAllCategories().size();
        int totalProduct = cDao.getTotalProduct();
        double avg = (totalCategory == 0) ? 0 : (double) totalProduct / totalCategory;

        request.setAttribute("categories", list);
        request.setAttribute("totalCategory", totalCategory);
        request.setAttribute("totalProduct", totalProduct);
        request.setAttribute("avgCategory", String.format("%.1f", avg));
        request.setAttribute("keyword", keyword);
        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("latestNotifications", oDao.getLatestAdminNotifications(20));

        request.getRequestDispatcher("/jsp/adminjsp/Admin_DanhMuc.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        CategoryDao cDao = new CategoryDao();
        String action = request.getParameter("action");
        boolean success = false;
        String message = null;

        if ("add".equals(action)) {
            String name = request.getParameter("name");
            String imageUrl = request.getParameter("imageUrl");

            if (name != null && !name.trim().isEmpty()) {
                cDao.addCategory(name.trim(), imageUrl);
                success = true;
            } else {
                message = "Vui lòng nhập tên danh mục.";
            }

            if (AjaxUtil.wantsJson(request)) {
                Map<String, Object> payload = success
                        ? AjaxUtil.ok("Đã thêm danh mục.")
                        : AjaxUtil.error(message);
                payload.put("action", action);
                AjaxUtil.writeJson(response, payload);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin/category");
            return;
        }

        if ("edit".equals(action)) {
            String idParam = request.getParameter("categoryId");
            String name = request.getParameter("name");
            String imageUrl = request.getParameter("imageUrl");

            if (idParam != null && !idParam.trim().isEmpty()
                    && name != null && !name.trim().isEmpty()) {
                int categoryId = Integer.parseInt(idParam);
                success = cDao.updateCategory(categoryId, name.trim(), imageUrl);
            } else {
                message = "Thông tin danh mục không hợp lệ.";
            }
            if (!success && message == null) {
                message = "Không tìm thấy danh mục cần cập nhật.";
            }

            if (AjaxUtil.wantsJson(request)) {
                Map<String, Object> payload = success
                        ? AjaxUtil.ok("Đã cập nhật danh mục.")
                        : AjaxUtil.error(message);
                payload.put("action", action);
                payload.put("categoryId", idParam);
                AjaxUtil.writeJson(response, payload);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin/category");
            return;
        }

        if (AjaxUtil.wantsJson(request)) {
            AjaxUtil.writeJson(response, AjaxUtil.error("Thao tác danh mục không hợp lệ."));
            return;
        }
        response.sendRedirect(request.getContextPath() + "/admin/category");
    }
}
