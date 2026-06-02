package controller;

import dao.OrderDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminReviewController", value = "/admin/reviews")
public class AdminReviewController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao oDao = new OrderDao();

        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("latestNotifications", oDao.getLatestAdminNotifications(5));

        request.setAttribute("totalReviews", 0);
        request.setAttribute("averageRating", 0);
        request.setAttribute("pendingReviews", 0);
        request.setAttribute("fiveStarPercent", 0);
        request.setAttribute("reviews", List.of());

        request.getRequestDispatcher("/jsp/adminjsp/Admin_DanhGia.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}