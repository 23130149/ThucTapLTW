package controller;

import dao.OrderDao;
import dao.ReviewDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Review;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminReviewController", value = "/admin/reviews")
public class AdminReviewController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao oDao = new OrderDao();
        ReviewDao rDao = new ReviewDao();

        String keyword = request.getParameter("keyword");
        keyword = keyword == null ? "" : keyword.trim();

        Integer rating = parseRating(request.getParameter("rating"));
        String status = normalizeStatus(request.getParameter("status"));

        List<Review> reviews = rDao.getAdminReviews(keyword, rating, status);
        Map<Integer, Integer> ratingCounts = rDao.countAllReviewsByRating();

        int totalReviews = rDao.countAllReviews();
        int pendingCount = rDao.countReviewsByStatus("PENDING");
        int fiveStarCount = ratingCounts.getOrDefault(5, 0);
        int fiveStarRate = totalReviews == 0 ? 0 : (int) Math.round(fiveStarCount * 100.0 / totalReviews);

        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("latestNotifications", oDao.getLatestAdminNotifications(5));

        request.setAttribute("reviews", reviews);
        request.setAttribute("totalReviews", totalReviews);
        request.setAttribute("averageRating", rDao.getAverageRatingAll());
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("fiveStarRate", fiveStarRate);
        request.setAttribute("ratingCounts", ratingCounts);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentRating", rating);
        request.setAttribute("currentStatus", status);

        request.getRequestDispatcher("/jsp/adminjsp/Admin_DanhGia.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
}