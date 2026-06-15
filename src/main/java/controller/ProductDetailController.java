package controller;

import dao.FavoriteDao;
import dao.OrderDao;
import dao.ProductDao;
import dao.ReviewDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;
import model.ProductImage;
import model.Review;
import model.User;
import util.RecaptchaUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "ProductDetailController", value = "/product-detail")
public class ProductDetailController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDao pDao = new ProductDao();
        FavoriteDao favoriteDao = new FavoriteDao();
        ReviewDao rDao = new ReviewDao();
        OrderDao orderDao = new OrderDao();
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
        List<Product> relatedProducts = pDao.getRelatedProducts(product.getCategoryId(), product.getProductId(), 8);

        HttpSession session = request.getSession(false);
        Integer currentUserId = null;
        boolean isLoggedIn = false;
        boolean canReview = false;

        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            currentUserId = user.getUserId();
            isLoggedIn = true;

            Set<Integer> favoriteIds = favoriteDao.getFavoriteProductIds(user.getUserId());
            product.setFavorite(favoriteIds.contains(product.getProductId()));
            relatedProducts.forEach(p -> p.setFavorite(favoriteIds.contains(p.getProductId())));

            canReview = orderDao.hasUserPurchasedProduct(user.getUserId(), productId);
        }

        Integer selectedRating = parseRating(request.getParameter("rating"));
        List<Review> reviews = rDao.getReviewsByProductId(productId, selectedRating, currentUserId);
        double avgRating = rDao.getAverageRating(productId);
        int reviewCount = rDao.countReviews(productId);
        Map<Integer, Integer> reviewRatingCounts = rDao.countReviewsByRating(productId);

        request.setAttribute("product", product);
        request.setAttribute("productImages", productImages);
        request.setAttribute("relatedProducts", relatedProducts);
        request.setAttribute("reviews", reviews);
        request.setAttribute("avgRating", avgRating);
        request.setAttribute("reviewCount", reviewCount);
        request.setAttribute("selectedRating", selectedRating);
        request.setAttribute("reviewRatingCounts", reviewRatingCounts);
        request.setAttribute("isLoggedIn", isLoggedIn);
        request.setAttribute("canReview", canReview);
        request.setAttribute("recaptchaSiteKey", RecaptchaUtil.getSiteKey(getServletContext()));
        request.setAttribute("recaptchaConfigured", RecaptchaUtil.isConfigured(getServletContext()));
        request.getRequestDispatcher("/jsp/productDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private Integer parseRating(String value) {
        try {
            int rating = Integer.parseInt(value);
            return rating >= 1 && rating <= 5 ? rating : null;
        } catch (Exception e) {
            return null;
        }
    }
}
