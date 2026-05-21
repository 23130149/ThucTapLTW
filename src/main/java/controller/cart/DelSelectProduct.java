package controller.cart;

import cart.Cart;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DelSelectProduct", value = "/DelSelectProduct")
public class DelSelectProduct extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        String[] ids = request.getParameterValues("productIds");
        int deletedCount = 0;

        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                try {
                    int productId = Integer.parseInt(id.trim());
                    if (productId > 0) {
                        cart.deleteProduct(productId);
                        deletedCount++;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (deletedCount > 0) {
            session.setAttribute("cartSuccess", "Đã xóa " + deletedCount + " sản phẩm");
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/cart");
    }

}