package controller;

import cart.Cart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import service.ProductService;

import java.io.IOException;

@WebServlet(name = "FavoriteSelectedCartController", value = "/favorite-selected-cart")
public class FavoriteSelectedCartController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String[] selectedIds = request.getParameterValues("selectedProductIds");
        HttpSession session = request.getSession();

        if (selectedIds == null || selectedIds.length == 0) {
            session.setAttribute("toastMessage", "Vui lòng chọn ít nhất một sản phẩm");
            session.setAttribute("toastType", "hh-toast-cart");
            session.setAttribute("toastIcon", "bx-cart-add");
            response.sendRedirect(request.getContextPath() + "/favorite");
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }

        ProductService productService = new ProductService();
        int addedCount = 0;

        for (String rawId : selectedIds) {
            try {
                int productId = Integer.parseInt(rawId);
                Product product = productService.getProductById(productId);
                if (product != null) {
                    cart.addProduct(product, 1);
                    addedCount++;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        session.setAttribute("cart", cart);

        if (addedCount == 0) {
            session.setAttribute("toastMessage", "Không có sản phẩm hợp lệ để thêm vào giỏ");
            session.setAttribute("toastType", "hh-toast-cart");
            session.setAttribute("toastIcon", "bx-cart-add");
            response.sendRedirect(request.getContextPath() + "/favorite");
            return;
        }

        String action = request.getParameter("action");
        if ("buy".equals(action)) {
            session.setAttribute("toastMessage", "Đã thêm " + addedCount + " sản phẩm đã chọn vào giỏ hàng");
            session.setAttribute("toastType", "hh-toast-cart");
            session.setAttribute("toastIcon", "bx-cart-add");
            response.sendRedirect(request.getContextPath() + "/payment");
            return;
        }

        session.setAttribute("toastMessage", "Đã thêm " + addedCount + " sản phẩm đã chọn vào giỏ hàng");
        session.setAttribute("toastType", "hh-toast-cart");
        session.setAttribute("toastIcon", "bx-cart-add");
        response.sendRedirect(request.getContextPath() + "/favorite");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/favorite");
    }
}
