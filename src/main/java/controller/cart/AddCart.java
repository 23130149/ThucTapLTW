package controller.cart;

import cart.Cart;
import dao.CartDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;
import model.User;
import service.ProductService;

import java.io.IOException;

@WebServlet(name = "AddCart", value = "/Add-Cart")
public class AddCart extends HttpServlet {
    private final CartDao cartDao = new CartDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("loginMessage", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng.");
            session.setAttribute("redirectAfterLogin", "cart");
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        String idRaw = request.getParameter("id");
        String qRaw = request.getParameter("quantity");

        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }

        int id;

        try {
            id = Integer.parseInt(idRaw.trim());

            if (id <= 0) {
                throw new NumberFormatException("Invalid product id");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }

        int quantity = 1;

        if (qRaw != null && !qRaw.trim().isEmpty()) {
            try {
                quantity = Math.max(1, Integer.parseInt(qRaw.trim()));
            } catch (NumberFormatException e) {
                quantity = 1;
            }
        }

        ProductService ps = new ProductService();
        Product p = ps.getProductById(id);

        if (p == null) {
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }

        cartDao.addProduct(user.getUserId(), id, quantity);
        Cart cart = cartDao.getCartByUserId(user.getUserId());

        session.setAttribute("cart", cart);
        session.setAttribute("toastMessage", "Đã thêm sản phẩm vào giỏ hàng");
        session.setAttribute("toastType", "hh-toast-cart");
        session.setAttribute("toastIcon", "bx-cart-add");

        if ("1".equals(request.getParameter("buyNow"))) {
            session.setAttribute("checkoutProductIds", java.util.Set.of(id));
            response.sendRedirect(request.getContextPath() + "/payment?productIds=" + id);
            return;
        }

        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : request.getContextPath() + "/product");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
