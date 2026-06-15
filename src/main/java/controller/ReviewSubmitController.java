package controller;

import dao.OrderDao;
import dao.ReviewDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.AjaxUtil;
import util.RecaptchaUtil;

import java.io.IOException;

@WebServlet(name = "ReviewSubmitController", value = "/review-submit")
public class ReviewSubmitController extends HttpServlet {
    private final ReviewDao reviewDao = new ReviewDao();
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        int productId = parseInt(request.getParameter("productId"), 0);
        if (productId <= 0) {
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Sản phẩm không hợp lệ."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }

        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("loginMessage", "Vui lòng đăng nhập để đánh giá sản phẩm.");
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Vui lòng đăng nhập để đánh giá sản phẩm."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        if (!orderDao.hasUserPurchasedProduct(user.getUserId(), productId)) {
            session.setAttribute("reviewError", "Bạn cần mua sản phẩm trước khi đánh giá.");
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Bạn cần mua sản phẩm trước khi đánh giá."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId);
            return;
        }

        if (RecaptchaUtil.isConfigured(getServletContext())
                && !RecaptchaUtil.verify(request, getServletContext())) {
            session.setAttribute("reviewError", "Vui lòng xác nhận bạn không phải robot.");
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Vui lòng xác nhận bạn không phải robot."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId);
            return;
        }

        int rating = parseInt(request.getParameter("rating"), 0);
        String comment = request.getParameter("comment");

        if (rating < 1 || rating > 5 || comment == null || comment.trim().length() < 5) {
            session.setAttribute("reviewError", "Vui lòng chọn số sao và nhập bình luận từ 5 ký tự trở lên.");
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Vui lòng chọn số sao và nhập bình luận từ 5 ký tự trở lên."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId);
            return;
        }

        reviewDao.addReview(productId, user.getUserId(), rating, comment.trim());
        session.setAttribute("reviewSuccess", "Đã gửi đánh giá của bạn. Cảm ơn bạn đã chia sẻ!");
        if (AjaxUtil.wantsJson(request)) {
            AjaxUtil.writeJson(response, AjaxUtil.ok("Đã gửi đánh giá của bạn. Shop sẽ duyệt trước khi hiển thị."));
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
