package controller;

import cart.Cart;
import dao.CartDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import model.User;
import service.ProductService;
import util.AjaxUtil;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "FavoriteSelectedCartController", value = "/favorite-selected-cart")
public class FavoriteSelectedCartController extends HttpServlet {
    private final CartDao cartDao = new CartDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String[] selectedIds = request.getParameterValues("selectedProductIds");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.setAttribute("loginMessage", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng.");
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        if (selectedIds == null || selectedIds.length == 0) {
            session.setAttribute("toastMessage", "Vui lòng chọn ít nhất một sản phẩm");
            session.setAttribute("toastType", "hh-toast-cart");
            session.setAttribute("toastIcon", "bx-cart-add");
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Vui lòng chọn ít nhất một sản phẩm."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/favorite");
            return;
        }

        ProductService productService = new ProductService();
        int addedCount = 0;

        for (String rawId : selectedIds) {
            try {
                int productId = Integer.parseInt(rawId);
                Product product = productService.getProductById(productId);
                if (product != null && product.getStockQuantity() != null && product.getStockQuantity() > 0) {
                    cartDao.addProduct(user.getUserId(), productId, 1);
                    addedCount++;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        Cart cart = cartDao.getCartByUserId(user.getUserId());
        session.setAttribute("cart", cart);

        if (addedCount == 0) {
            session.setAttribute("toastMessage", "Không có sản phẩm hợp lệ để thêm vào giỏ");
            session.setAttribute("toastType", "hh-toast-cart");
            session.setAttribute("toastIcon", "bx-cart-add");
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Không có sản phẩm hợp lệ để thêm vào giỏ."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/favorite");
            return;
        }

        String action = request.getParameter("action");
        if (AjaxUtil.wantsJson(request)) {
            Map<String, Object> payload = AjaxUtil.ok("Đã thêm " + addedCount + " sản phẩm đã chọn vào giỏ hàng.");
            payload.put("addedCount", addedCount);
            payload.put("cart", AjaxUtil.cartSummary(cart));
            payload.put("redirect", "buy".equals(action) ? request.getContextPath() + "/payment" : "");
            AjaxUtil.writeJson(response, payload);
            return;
        }

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
