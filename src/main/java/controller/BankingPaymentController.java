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

@WebServlet(name = "BankingPaymentController", value = "/banking-payment")
public class BankingPaymentController extends HttpServlet {

    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn?redirect=payment");
            return;
        }

        String orderCode = request.getParameter("orderCode");

        if (orderCode == null || orderCode.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/OrderHistory");
            return;
        }

        User user = (User) session.getAttribute("user");
        Order order = orderDao.getOrderByCodeAndUser(orderCode.trim(), user.getUserId());

        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/OrderHistory");
            return;
        }

        request.setAttribute("order", order);
        request.getRequestDispatcher("/jsp/banking_payment.jsp").forward(request, response);
    }
}
