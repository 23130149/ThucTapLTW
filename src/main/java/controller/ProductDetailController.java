package controller;

import dao.FavoriteDao;
import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;
import model.ProductImage;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "ProductDetailController", value = "/product-detail")
public class ProductDetailController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDao pDao = new ProductDao();
        FavoriteDao favoriteDao = new FavoriteDao();
        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }

        Product product = pDao.getProductById(productId);
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sản phẩm không tồn tại");
            return;
        }

        List<ProductImage> productImages = pDao.getImagesByProductId(productId);
        List<Product> relatedProducts = pDao.getRelatedProducts(product.getCategoryId(), product.getProductId(), 4);

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            Set<Integer> favoriteIds = favoriteDao.getFavoriteProductIds(user.getUserId());
            product.setFavorite(favoriteIds.contains(product.getProductId()));
            relatedProducts.forEach(p -> p.setFavorite(favoriteIds.contains(p.getProductId())));
        }

        request.setAttribute("product", product);
        request.setAttribute("productImages", productImages);
        request.setAttribute("relatedProducts", relatedProducts);
        request.setAttribute("avgRating", 5);
        request.setAttribute("reviewCount", 0);
        request.getRequestDispatcher("/jsp/productDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
