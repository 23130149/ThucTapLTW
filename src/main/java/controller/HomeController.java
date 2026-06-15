package controller;

import dao.CategoryDao;
import dao.FavoriteDao;
import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;
import model.User;
import dao.BannerDao;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "HomeController", value = "/home")
public class HomeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryDao cDao = new CategoryDao();
        ProductDao pDao = new ProductDao();
        FavoriteDao favoriteDao = new FavoriteDao();
        BannerDao bDao = new BannerDao();

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        List<Product> featuredProducts = pDao.getFeaturedProducts(user == null ? -1 : user.getUserId(), 8);

        if (session != null && session.getAttribute("user") != null) {
            Set<Integer> favoriteIds = favoriteDao.getFavoriteProductIds(user.getUserId());
            featuredProducts.forEach(p -> p.setFavorite(favoriteIds.contains(p.getProductId())));
        }

        request.setAttribute("bannerList", bDao.findActiveBanners());
        request.setAttribute("categoryList", cDao.getAllCategories());
        request.setAttribute("productList", featuredProducts);
        request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
