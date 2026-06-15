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

@WebServlet(name = "ReviewReplyController", value = "/review-reply")
public class ReviewReplyController extends HttpServlet {
    private final ReviewDao reviewDao = new ReviewDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        int productId = parseInt(request.getParameter("productId"), 0);
        int reviewId = parseInt(request.getParameter("reviewId"), 0);
        String replyText = request.getParameter("replyText");

        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("loginMessage", "Vui lòng đăng nhập để trả lời bình luận.");
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Vui lòng đăng nhập để trả lời đánh giá."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        boolean allowed = reviewId > 0 && productId > 0 && reviewDao.isApprovedReviewForProduct(reviewId, productId);
        boolean success = false;
        if (allowed && replyText != null && replyText.trim().length() >= 2) {
            reviewDao.addReply(reviewId, user.getUserId(), replyText.trim());
            success = true;
        }

        if (AjaxUtil.wantsJson(request)) {
            if (!allowed) {
                Map<String, Object> payload = AjaxUtil.error("Không tìm thấy đánh giá.");
                payload.put("reviewId", reviewId);
                AjaxUtil.writeJson(response, payload);
                return;
            }

            Map<String, Object> payload = success
                    ? AjaxUtil.ok("Đã gửi phản hồi.")
                    : AjaxUtil.error("Phản hồi phải có ít nhất 2 ký tự.");
            payload.put("reviewId", reviewId);
            if (success) {
                String userName = user.getUserName();
                if (userName == null || userName.isBlank()) {
                    userName = user.getEmail();
                }
                payload.put("userName", userName == null || userName.isBlank() ? "Khách hàng" : userName);
                payload.put("replyText", replyText.trim());
            }
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
