package controller.cart;

import cart.Cart;
import cart.CartItem;
import dao.CartDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import service.ProductService;

import java.io.IOException;

@WebServlet(name = "CartUpdate", value = "/CartUpdate")
public class CartUpdate extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final CartDao cartDao = new CartDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("loginMessage", "Vui lòng đăng nhập để cập nhật giỏ hàng.");
            session.setAttribute("redirectAfterLogin", "cart");
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        Cart cart = cartDao.getCartByUserId(user.getUserId());
        session.setAttribute("cart", cart);

        if (cart == null) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        String productIdRaw = request.getParameter("productId");
        String action = request.getParameter("action");

        int productId;
        try {
            productId = Integer.parseInt(productIdRaw);
        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        CartItem item = cart.getItem(productId);
        if (item == null) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        int currentQuantity = item.getQuantity();
        int stockQuantity = productService.getStockById(productId);

        if ("inc".equals(action) && currentQuantity < stockQuantity) {
            currentQuantity++;
        } else if ("dec".equals(action) && currentQuantity > 1) {
            currentQuantity--;
        }

        cartDao.updateQuantity(user.getUserId(), productId, currentQuantity);
        session.setAttribute("cart", cartDao.getCartByUserId(user.getUserId()));
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
