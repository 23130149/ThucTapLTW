package controller;

import dao.CategoryDao;
import dao.FavoriteDao;
import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "ProductController", value = "/product")
public class ProductController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        String categoryId = request.getParameter("categoryId");

        ProductDao productDao = new ProductDao();
        CategoryDao categoryDao = new CategoryDao();
        FavoriteDao favoriteDao = new FavoriteDao();

        List<Product> products;
        if ((keyword != null && !keyword.trim().isEmpty()) || (categoryId != null && !categoryId.trim().isEmpty())) {
            products = productDao.getFilteredProducts(keyword, categoryId, null, null, 1, 500);
        } else {
            products = productDao.getListProduct();
        }

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            Set<Integer> favoriteIds = favoriteDao.getFavoriteProductIds(user.getUserId());
            products.forEach(p -> p.setFavorite(favoriteIds.contains(p.getProductId())));
        }

        request.setAttribute("productList", products);
        request.setAttribute("categoryList", categoryDao.getAllCategories());
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCategoryId", categoryId);
        request.setAttribute("productCount", products.size());

        request.getRequestDispatcher("/jsp/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
