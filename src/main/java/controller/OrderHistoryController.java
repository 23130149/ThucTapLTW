package controller;

import dao.OrderDao;
import model.Order;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/OrderHistory")
public class OrderHistoryController extends HttpServlet {

    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        User user = (User) session.getAttribute("user");

        int page = 1;
        int pageSize = 10;

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
        } catch (Exception ignored) {}

        if (page < 1) {
            page = 1;
        }

        int totalOrders = orderDao.countOrdersByUserId(user.getUserId());
        int totalPages = (int) Math.ceil(totalOrders * 1.0 / pageSize);

        if (totalPages == 0) {
            totalPages = 1;
        }

        if (page > totalPages) {
            page = totalPages;
        }

        int offset = (page - 1) * pageSize;

        List<Order> orderList =
                orderDao.getOrdersByUserIdPaged(user.getUserId(), pageSize, offset);

        request.setAttribute("orderList", orderList != null ? orderList : new ArrayList<>());
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalOrders", totalOrders);

        request.setAttribute("orderList", orderList != null ? orderList : new ArrayList<>());

        request.getRequestDispatcher("/jsp/orderhistory.jsp")
                .forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        String action = req.getParameter("action");

        if ("complete".equals(action)) {
            orderDao.updateStatus(orderId, "COMPLETED");
        } else if ("cancel".equals(action)) {
            orderDao.updateStatus(orderId, "CANCELLED");
        }

        resp.sendRedirect("OrderHistory");
    }

}
