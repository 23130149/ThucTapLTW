package controller;

import dao.OrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/Account")
public class AccountController extends HttpServlet {

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

        List<Order> recentOrders =
                orderDao.getOrdersByUserId(user.getUserId())
                        .stream()
                        .limit(3)
                        .toList();


        request.setAttribute("orderList", recentOrders);

        request.getRequestDispatcher("/jsp/account.jsp")
                .forward(request, response);
    }
}
