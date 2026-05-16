package controller;

import dao.OrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Order;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Account")
public class AccountController extends HttpServlet {

    private final OrderDao orderDao = new OrderDao();

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

        int recentLimit = 5;
        String recent = request.getParameter("recent");

        if ("10".equals(recent)) {
            recentLimit = 10;
        }

        List<Order> orderList = orderDao.getRecentOrdersByUser(user.getUserId(), recentLimit);

        request.setAttribute("orderList", orderList != null ? orderList : new ArrayList<>());
        request.setAttribute("recentLimit", recentLimit);

        request.getRequestDispatcher("/jsp/myaccount.jsp")
                .forward(request, response);
    }
}
