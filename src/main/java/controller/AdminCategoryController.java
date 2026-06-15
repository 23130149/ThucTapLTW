package controller;

import dao.CategoryDao;
import dao.OrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            handleDelete(request, response, cDao, request.getParameter("id"));
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

            writeResultOrRedirect(request, response, success, message, action, null,
                    "Đã thêm danh mục.");
            return;
        }

        if ("edit".equals(action)) {
            String idParam = request.getParameter("categoryId");
            String name = request.getParameter("name");
            String imageUrl = request.getParameter("imageUrl");

            if (idParam != null && !idParam.trim().isEmpty()
                    && name != null && !name.trim().isEmpty()) {
                try {
                    int categoryId = Integer.parseInt(idParam);
                    success = cDao.updateCategory(categoryId, name.trim(), imageUrl);
                } catch (NumberFormatException e) {
                    message = "Mã danh mục không hợp lệ.";
                }
            } else {
                message = "Thông tin danh mục không hợp lệ.";
            }

            if (!success && message == null) {
                message = "Không tìm thấy danh mục cần cập nhật.";
            }

            writeResultOrRedirect(request, response, success, message, action, idParam,
                    "Đã cập nhật danh mục.");
            return;
        }

        if ("delete".equals(action)) {
            handleDelete(request, response, cDao, request.getParameter("categoryId"));
            return;
        }

        if (AjaxUtil.wantsJson(request)) {
            AjaxUtil.writeJson(response, AjaxUtil.error("Thao tác danh mục không hợp lệ."));
            return;
        }
        response.sendRedirect(request.getContextPath() + "/admin/category");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response,
                              CategoryDao cDao, String idParam) throws IOException {
        boolean success = false;
        String message = null;

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int categoryId = Integer.parseInt(idParam);
                if (cDao.hasProducts(categoryId)) {
                    message = "Không thể xóa danh mục đang chứa sản phẩm.";
                } else {
                    success = cDao.deleteCategory(categoryId);
                }
            } catch (NumberFormatException e) {
                message = "Mã danh mục không hợp lệ.";
            }
        } else {
            message = "Thông tin danh mục không hợp lệ.";
        }

        if (!success && message == null) {
            message = "Không tìm thấy danh mục cần xóa.";
        }

        writeResultOrRedirect(request, response, success, message, "delete", idParam,
                "Đã xóa danh mục.");
    }

    private void writeResultOrRedirect(HttpServletRequest request, HttpServletResponse response,
                                       boolean success, String message, String action,
                                       String categoryId, String successMessage) throws IOException {
        if (AjaxUtil.wantsJson(request)) {
            Map<String, Object> payload = success
                    ? AjaxUtil.ok(successMessage)
                    : AjaxUtil.error(message);
            payload.put("action", action);
            if (categoryId != null) {
                payload.put("categoryId", categoryId);
            }
            AjaxUtil.writeJson(response, payload);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/admin/category");
    }
}
