package controller;

import dao.CategoryDao;
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

        List<Category> list = cDao.getAllCategories();
        int totalCategory = list.size();
        int totalProduct = cDao.getTotalProduct();
        double avg = (totalCategory == 0) ? 0 : (double) totalProduct / totalCategory;

        request.setAttribute("categories", list);
        request.setAttribute("totalCategory", totalCategory);
        request.setAttribute("totalProduct", totalProduct);
        request.setAttribute("avgCategory", String.format("%.1f", avg));
        request.getRequestDispatcher("/jsp/adminjsp/Admin_DanhMuc.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}