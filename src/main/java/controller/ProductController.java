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
    private int parsePage(String rawPage) {
        try {
            int page = Integer.parseInt(rawPage);
            return Math.max(page, 1);
        } catch (Exception e) {
            return 1;
        }
    }

    private String clean(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String keyword = clean(request.getParameter("keyword"));
        String categoryId = clean(request.getParameter("categoryId"));
        String status = clean(request.getParameter("status"));
        String priceRange = clean(request.getParameter("priceRange"));
        String material = clean(request.getParameter("material"));
        String usage = clean(request.getParameter("usage"));
        String sort = clean(request.getParameter("sort"));

        ProductDao productDao = new ProductDao();
        CategoryDao categoryDao = new CategoryDao();
        FavoriteDao favoriteDao = new FavoriteDao();

        int pageSize = 8;
        int currentPage = parsePage(request.getParameter("page"));
        int productCount = productDao.countFilteredProducts(keyword, categoryId, status, priceRange, material, usage);
        int totalPages = Math.max(1, (int) Math.ceil((double) productCount / pageSize));

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        List<Product> products = productDao.getFilteredProducts(keyword, categoryId, status, priceRange, material, usage, sort, currentPage, pageSize);

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
        request.setAttribute("status", status);
        request.setAttribute("priceRange", priceRange);
        request.setAttribute("material", material);
        request.setAttribute("usage", usage);
        request.setAttribute("sort", sort);
        request.setAttribute("productCount", productCount);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/jsp/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
