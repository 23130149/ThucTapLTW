package controller.cart;

import cart.Cart;
import dao.CartDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "ViewCart", value = "/cart")
public class ViewCart extends HttpServlet {
    private final CartDao cartDao = new CartDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("redirectAfterLogin", "cart");
            request.setAttribute("loginRequired", true);
            request.getRequestDispatcher("/jsp/cart.jsp").forward(request, response);
            return;
        }

        Cart cart = cartDao.getCartByUserId(user.getUserId());
        session.setAttribute("cart", cart);
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/jsp/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Post is not supported for /cart");
    }
}
