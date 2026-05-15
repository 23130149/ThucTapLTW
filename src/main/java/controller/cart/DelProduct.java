package controller.cart;

import cart.Cart;
import cart.CartItem;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "DelProduct", value = "/DelProduct")
public class DelProduct extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DelProduct.class.getName());
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        if (idRaw == null || idRaw.trim().isEmpty()) {
            request.getSession().setAttribute("cartError", "Thiếu tham số id");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idRaw.trim());
            if (id <= 0) {
                throw new NumberFormatException("id must be positive");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid product id for deletion: " + idRaw, e);
            request.getSession().setAttribute("cartError", "ID sản phẩm không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        HttpSession session = request.getSession();
        Cart c = (Cart) session.getAttribute("cart");
        if (c == null) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        CartItem cartItem = c.deleteProduct(id);
        if (cartItem == null) {
            session.setAttribute("cartError", "Sản phẩm không tồn tại trong giỏ hàng");
        } else {
            session.setAttribute("cartSuccess", "Đã xóa sản phẩm khỏi giỏ hàng");
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
