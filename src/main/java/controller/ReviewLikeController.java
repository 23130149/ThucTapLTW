package controller;

import dao.ReviewDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.AjaxUtil;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ReviewLikeController", value = "/review-like")
public class ReviewLikeController extends HttpServlet {
    private final ReviewDao reviewDao = new ReviewDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        int productId = parseInt(request.getParameter("productId"), 0);
        int reviewId = parseInt(request.getParameter("reviewId"), 0);

        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("loginMessage", "Vui lòng đăng nhập để thích bình luận.");
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Vui lòng đăng nhập để thích bình luận."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        boolean allowed = reviewId > 0 && productId > 0 && reviewDao.isApprovedReviewForProduct(reviewId, productId);

        if (allowed) {
            reviewDao.toggleLike(reviewId, user.getUserId());
        }

        if (AjaxUtil.wantsJson(request)) {
            Map<String, Object> payload = allowed
                    ? AjaxUtil.ok("Đã cập nhật lượt thích.")
                    : AjaxUtil.error("Không tìm thấy đánh giá.");
            payload.put("reviewId", reviewId);
            payload.put("liked", allowed && reviewDao.hasUserLiked(reviewId, user.getUserId()));
            payload.put("helpfulCount", allowed ? reviewDao.countLikes(reviewId) : 0);
            AjaxUtil.writeJson(response, payload);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId);
    }

    private int parseInt(String raw, int fallback) {
        try {
            return Integer.parseInt(raw);
        } catch (Exception e) {
            return fallback;
        }
    }
}
