package controller;

import dao.FavoriteDao;
import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "ProductDetailController", value = "/product-detail")
public class ProductDetailController extends HttpServlet {
    private ProductDao productDao;
    private FavoriteDao favoriteDao;

    @Override
    public void init() {
        productDao = new ProductDao();
        favoriteDao = new FavoriteDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId;
        try {
            productId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }

        Product product = productDao.getProductById(productId);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }

        List<Product> relatedProducts = productDao.getRelatedProducts(product.getCategoryId(), product.getProductId(), 8);

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            Set<Integer> favoriteIds = favoriteDao.getFavoriteProductIds(user.getUserId());
            product.setFavorite(favoriteIds.contains(product.getProductId()));
            relatedProducts.forEach(p -> p.setFavorite(favoriteIds.contains(p.getProductId())));
        }

        request.setAttribute("product", product);
        request.setAttribute("relatedProducts", relatedProducts);
        request.getRequestDispatcher("/jsp/productDetail.jsp").forward(request, response);
    }
}
