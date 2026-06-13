package controller.cart;

import cart.Cart;
import cart.CartItem;
import dao.CartDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import service.ProductService;
import util.AjaxUtil;

import java.io.IOException;
import java.util.Map;

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
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Vui lòng đăng nhập để cập nhật giỏ hàng."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        Cart cart = cartDao.getCartByUserId(user.getUserId());
        session.setAttribute("cart", cart);

        if (cart == null) {
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Không tìm thấy giỏ hàng."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        String productIdRaw = request.getParameter("productId");
        String action = request.getParameter("action");

        int productId;
        try {
            productId = Integer.parseInt(productIdRaw);
        } catch (NumberFormatException | NullPointerException e) {
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Sản phẩm không hợp lệ."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        CartItem item = cart.getItem(productId);
        if (item == null) {
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Sản phẩm không có trong giỏ hàng."));
                return;
            }
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
        Cart updatedCart = cartDao.getCartByUserId(user.getUserId());
        session.setAttribute("cart", updatedCart);
        if (AjaxUtil.wantsJson(request)) {
            CartItem updatedItem = updatedCart.getItem(productId);
            Map<String, Object> payload = AjaxUtil.ok("Đã cập nhật giỏ hàng.");
            payload.put("productId", productId);
            payload.put("quantity", updatedItem == null ? 0 : updatedItem.getQuantity());
            payload.put("itemTotal", updatedItem == null ? 0 : updatedItem.getTotal());
            payload.put("cart", AjaxUtil.cartSummary(updatedCart));
            AjaxUtil.writeJson(response, payload);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
