package controller.cart;

import cart.Cart;
import cart.CartItem;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import service.ProductService;

import java.io.IOException;

@WebServlet(name = "CartUpdate", value = "/CartUpdate")
public class CartUpdate extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

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

        cart.update(productId, currentQuantity);
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}