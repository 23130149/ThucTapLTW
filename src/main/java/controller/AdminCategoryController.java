package controller;

import dao.CategoryDao;
import dao.OrderDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Category;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminCategoryController", value = "/admin/category")
public class AdminCategoryController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryDao cDao = new CategoryDao();
        OrderDao oDao = new OrderDao();

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int categoryId = Integer.parseInt(request.getParameter("id"));
            cDao.deleteCategory(categoryId);
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

        if ("add".equals(action)) {
            String name = request.getParameter("name");
            String imageUrl = request.getParameter("imageUrl");

            if (name != null && !name.trim().isEmpty()) {
                cDao.addCategory(name.trim(), imageUrl);
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
                cDao.updateCategory(categoryId, name.trim(), imageUrl);
            }

            response.sendRedirect(request.getContextPath() + "/admin/category");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/admin/category");
    }
}