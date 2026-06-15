package controller;

import dao.CategoryDao;
import dao.NotificationDao;
import dao.OrderDao;
import dao.ReviewDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Review;
import model.ReviewReply;
import util.AjaxUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminReviewController", value = "/admin/reviews")
public class AdminReviewController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao oDao = new OrderDao();
        ReviewDao rDao = new ReviewDao();
        CategoryDao cDao = new CategoryDao();

        String keyword = request.getParameter("keyword");
        keyword = keyword == null ? "" : keyword.trim();

        Integer rating = parseRating(request.getParameter("rating"));
        String status = normalizeStatus(request.getParameter("status"));
        Integer categoryId = parsePositiveInt(request.getParameter("categoryId"));

        List<Review> reviews = rDao.getAdminReviews(keyword, rating, status, categoryId);
        Map<Integer, Integer> ratingCounts = rDao.countAllReviewsByRating();

        for (int i = 1; i <= 5; i++) {
            ratingCounts.putIfAbsent(i, 0);
        }

        int totalReviews = rDao.countAllReviews();
        int pendingCount = rDao.countPendingModerationItems();
        int fiveStarCount = ratingCounts.getOrDefault(5, 0);
        int fourStarCount = ratingCounts.getOrDefault(4, 0);
        int threeStarCount = ratingCounts.getOrDefault(3, 0);
        int twoStarCount = ratingCounts.getOrDefault(2, 0);
        int oneStarCount = ratingCounts.getOrDefault(1, 0);
        int fiveStarRate = totalReviews == 0 ? 0 : (int) Math.round(fiveStarCount * 100.0 / totalReviews);

        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("latestNotifications", oDao.getLatestAdminNotifications(20));

        request.setAttribute("reviews", reviews);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentRating", rating);
        request.setAttribute("currentStatus", status);
        request.setAttribute("currentCategoryId", categoryId);
        request.setAttribute("categories", cDao.getAllCategories());
        request.setAttribute("totalReviews", totalReviews);
        request.setAttribute("averageRating", rDao.getAverageRatingAll());
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("fiveStarCount", fiveStarCount);
        request.setAttribute("fourStarCount", fourStarCount);
        request.setAttribute("threeStarCount", threeStarCount);
        request.setAttribute("twoStarCount", twoStarCount);
        request.setAttribute("oneStarCount", oneStarCount);
        request.setAttribute("fiveStarRate", fiveStarRate);
        request.setAttribute("ratingCounts", ratingCounts);


        request.getRequestDispatcher("/jsp/adminjsp/Admin_DanhGia.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        ReviewDao rDao = new ReviewDao();
        NotificationDao notificationDao = new NotificationDao();
        String action = request.getParameter("action");
        boolean success = false;
        String message = null;

        if ("reply".equals(action)) {
            int reviewId = parseInt(request.getParameter("reviewId"), 0);
            String replyText = request.getParameter("replyText");

            if (reviewId > 0 && replyText != null && !replyText.trim().isEmpty()) {
                success = rDao.updateShopReply(reviewId, replyText.trim());
                message = success ? "Đã lưu phản hồi đánh giá thành công" : "Không tìm thấy đánh giá cần phản hồi";
                if (success) {
                    Review review = rDao.findReviewNotificationInfo(reviewId);
                    if (review != null && review.getUserId() != null && review.getUserId() > 0
                            && "APPROVED".equalsIgnoreCase(review.getStatus())) {
                        notificationDao.addOrRefreshSafe(
                                review.getUserId(),
                                "REVIEW_REPLY",
                                "Shop đã phản hồi đánh giá của bạn",
                                "Sản phẩm: " + safeText(review.getProductName()),
                                "/product-detail?id=" + review.getProductId() + "#review-" + reviewId,
                                "REVIEW",
                                reviewId
                        );
                    }
                }
            } else {
                message = "Vui lòng nhập nội dung phản hồi";
            }
        } else if ("approveReply".equals(action) || "hideReply".equals(action)) {
            int replyId = parseInt(request.getParameter("replyId"), 0);
            String newStatus = "approveReply".equals(action) ? "APPROVED" : "HIDDEN";

            if (replyId > 0) {
                ReviewReply reply = rDao.findReplyById(replyId);
                success = rDao.updateReplyStatus(replyId, newStatus);
                message = success
                        ? ("APPROVED".equals(newStatus)
                            ? "Đã duyệt phản hồi của người dùng"
                            : "Đã ẩn phản hồi của người dùng")
                        : "Không tìm thấy phản hồi cần xử lý";

                if (success && "APPROVED".equals(newStatus) && reply != null) {
                    Review review = rDao.findReviewNotificationInfo(reply.getReviewId());
                    if (review != null && review.getUserId() != null && review.getUserId() > 0
                            && reply.getUserId() != null && !review.getUserId().equals(reply.getUserId())) {
                        notificationDao.addSafe(
                                review.getUserId(),
                                "REVIEW_REPLY",
                                safeText(reply.getUserName()) + " đã trả lời đánh giá của bạn",
                                "Sản phẩm: " + safeText(review.getProductName()),
                                "/product-detail?id=" + review.getProductId() + "#review-" + review.getReviewId()
                        );
                    }
                }
            } else {
                message = "Không tìm thấy phản hồi cần xử lý";
            }
        } else if ("approve".equals(action) || "hide".equals(action)) {
            int reviewId = parseInt(request.getParameter("reviewId"), 0);
            String newStatus = "approve".equals(action) ? "APPROVED" : "HIDDEN";

            if (reviewId > 0) {
                success = rDao.updateReviewStatus(reviewId, newStatus);
                message = success
                        ? ("APPROVED".equals(newStatus)
                            ? "Đã duyệt đánh giá và hiển thị trên trang sản phẩm"
                            : "Đã ẩn đánh giá khỏi trang sản phẩm")
                        : "Không tìm thấy đánh giá cần xử lý";
                if (success) {
                    Review review = rDao.findReviewNotificationInfo(reviewId);
                    if (review != null && review.getUserId() != null && review.getUserId() > 0) {
                        if ("APPROVED".equals(newStatus)) {
                            boolean hasShopReply = review.getShopReply() != null && !review.getShopReply().isBlank();
                            notificationDao.addOrRefreshSafe(
                                    review.getUserId(),
                                    hasShopReply ? "REVIEW_REPLY" : "REVIEW_STATUS",
                                    hasShopReply ? "Đánh giá của bạn đã được duyệt và có phản hồi" : "Đánh giá của bạn đã được duyệt",
                                    "Sản phẩm: " + safeText(review.getProductName()),
                                    "/product-detail?id=" + review.getProductId() + "#review-" + reviewId,
                                    "REVIEW",
                                    reviewId
                            );
                        } else {
                            notificationDao.addOrRefreshSafe(
                                    review.getUserId(),
                                    "REVIEW_STATUS",
                                    "Đánh giá của bạn đã được ẩn",
                                    "Sản phẩm: " + safeText(review.getProductName()),
                                    "/product-detail?id=" + review.getProductId(),
                                    "REVIEW",
                                    reviewId
                            );
                        }
                    }
                }
            } else {
                message = "Không tìm thấy đánh giá cần xử lý";
            }
        } else {
            message = "Thao tác đánh giá không hợp lệ";
        }

        if (success) {
            request.getSession().setAttribute("reviewMessage", message);
        } else {
            request.getSession().setAttribute("reviewError", message);
        }

        if (AjaxUtil.wantsJson(request)) {
            Map<String, Object> payload = success
                    ? AjaxUtil.ok(message)
                    : AjaxUtil.error(message == null ? "Không thể cập nhật đánh giá." : message);
            payload.put("action", action);
            payload.put("reviewId", request.getParameter("reviewId"));
            AjaxUtil.writeJson(response, payload);
            return;
        }

        response.sendRedirect(buildReviewRedirect(request));
    }


    private String safeText(String value) {
        if (value == null || value.isBlank()) {
            return "sản phẩm";
        }
        return value.trim();
    }

    private Integer parseRating(String value) {
        int rating = parseInt(value, 0);
        return rating >= 1 && rating <= 5 ? rating : null;
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return value == null || value.isBlank() ? defaultValue : Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Integer parsePositiveInt(String value) {
        int result = parseInt(value, 0);
        return result > 0 ? result : null;
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        status = status.trim().toUpperCase();

        if ("PENDING".equals(status) || "APPROVED".equals(status) || "HIDDEN".equals(status)) {
            return status;
        }

        return null;
    }

    private String buildReviewRedirect(HttpServletRequest request) {
        String keyword = request.getParameter("keyword");
        String rating = request.getParameter("rating");
        String status = request.getParameter("status");
        String categoryId = request.getParameter("categoryId");

        StringBuilder redirect = new StringBuilder(request.getContextPath()).append("/admin/reviews");
        boolean hasParam = false;

        if (keyword != null && !keyword.isBlank()) {
            redirect.append(hasParam ? "&" : "?")
                    .append("keyword=")
                    .append(URLEncoder.encode(keyword.trim(), StandardCharsets.UTF_8));
            hasParam = true;
        }

        if (rating != null && !rating.isBlank()) {
            redirect.append(hasParam ? "&" : "?")
                    .append("rating=")
                    .append(URLEncoder.encode(rating.trim(), StandardCharsets.UTF_8));
            hasParam = true;
        }

        if (status != null && !status.isBlank()) {
            redirect.append(hasParam ? "&" : "?")
                    .append("status=")
                    .append(URLEncoder.encode(status.trim(), StandardCharsets.UTF_8));
            hasParam = true;
        }

        if (categoryId != null && !categoryId.isBlank()) {
            redirect.append(hasParam ? "&" : "?")
                    .append("categoryId=")
                    .append(URLEncoder.encode(categoryId.trim(), StandardCharsets.UTF_8));
        }

        return redirect.toString();
    }
}
