package controller.cart;

import cart.Cart;
import dao.CartDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import util.AjaxUtil;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "DelProduct", value = "/DelProduct")
public class DelProduct extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DelProduct.class.getName());
    private final CartDao cartDao = new CartDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("loginMessage", "Vui lòng đăng nhập để xóa sản phẩm trong giỏ hàng.");
            session.setAttribute("redirectAfterLogin", "cart");
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Vui lòng đăng nhập để xóa sản phẩm trong giỏ hàng."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        String idRaw = request.getParameter("id");
        if (idRaw == null || idRaw.trim().isEmpty()) {
            session.setAttribute("cartError", "Thiếu tham số id");
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Thiếu tham số id."));
                return;
            }
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
            session.setAttribute("cartError", "ID sản phẩm không hợp lệ");
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("ID sản phẩm không hợp lệ."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        boolean deleted = cartDao.removeProduct(user.getUserId(), id);
        if (deleted) {
            session.setAttribute("toastMessage", "Đã xóa sản phẩm khỏi giỏ hàng");
            session.setAttribute("toastType", "hh-toast-cart");
            session.setAttribute("toastIcon", "bx-trash");
        } else {
            session.setAttribute("cartError", "Sản phẩm không tồn tại trong giỏ hàng");
        }

        Cart cart = cartDao.getCartByUserId(user.getUserId());
        session.setAttribute("cart", cart);
        if (AjaxUtil.wantsJson(request)) {
            Map<String, Object> payload = deleted
                    ? AjaxUtil.ok("Đã xóa sản phẩm khỏi giỏ hàng.")
                    : AjaxUtil.error("Sản phẩm không tồn tại trong giỏ hàng.");
            payload.put("productId", id);
            payload.put("cart", AjaxUtil.cartSummary(cart));
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
