package controller.cart;

import cart.Cart;
import dao.CartDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;
import model.User;
import service.ProductService;
import util.AjaxUtil;

import java.io.IOException;
import java.util.Map;

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
            String referer = request.getHeader("Referer");
            session.setAttribute("showLoginModal", true);
            session.setAttribute("redirectAfterLogin", getLocalRedirect(request, referer));
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng."));
                return;
            }
            response.sendRedirect(getLocalReferer(request, referer));
            return;
        }

        String idRaw = request.getParameter("id");
        String qRaw = request.getParameter("quantity");

        if (idRaw == null || idRaw.trim().isEmpty()) {
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Thiếu sản phẩm cần thêm vào giỏ hàng."));
                return;
            }
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
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Sản phẩm không hợp lệ."));
                return;
            }
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
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Không tìm thấy sản phẩm."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }

        int stockQuantity = p.getStockQuantity() == null ? 0 : p.getStockQuantity();
        if (stockQuantity <= 0) {
            String message = "Sản phẩm đã hết hàng.";
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error(message));
                return;
            }
            session.setAttribute("toastMessage", message);
            session.setAttribute("toastType", "hh-toast-cart");
            session.setAttribute("toastIcon", "bx-error-circle");
            response.sendRedirect(getLocalReferer(request, request.getHeader("Referer")));
            return;
        }

        quantity = Math.min(quantity, stockQuantity);
        cartDao.addProduct(user.getUserId(), id, quantity);
        Cart cart = cartDao.getCartByUserId(user.getUserId());

        session.setAttribute("cart", cart);

        if (AjaxUtil.wantsJson(request) && !"1".equals(request.getParameter("buyNow"))) {
            Map<String, Object> payload = AjaxUtil.ok("Đã thêm sản phẩm vào giỏ hàng.");
            payload.put("productId", id);
            payload.put("cart", AjaxUtil.cartSummary(cart));
            AjaxUtil.writeJson(response, payload);
            return;
        }

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

    private String getLocalReferer(HttpServletRequest request, String referer) {
        String fallback = request.getContextPath() + "/product";

        if (referer == null || referer.isBlank()) {
            return fallback;
        }

        String appBase = request.getScheme() + "://" + request.getServerName()
                + (isDefaultPort(request) ? "" : ":" + request.getServerPort())
                + request.getContextPath();

        return referer.startsWith(appBase) ? referer : fallback;
    }

    private String getLocalRedirect(HttpServletRequest request, String referer) {
        String localReferer = getLocalReferer(request, referer);
        String contextPath = request.getContextPath();

        if (localReferer.startsWith(contextPath)) {
            return stripLeadingSlash(localReferer.substring(contextPath.length()));
        }

        String appBase = request.getScheme() + "://" + request.getServerName()
                + (isDefaultPort(request) ? "" : ":" + request.getServerPort())
                + contextPath;

        if (localReferer.startsWith(appBase)) {
            return stripLeadingSlash(localReferer.substring(appBase.length()));
        }

        return "product";
    }

    private boolean isDefaultPort(HttpServletRequest request) {
        int port = request.getServerPort();
        return ("http".equalsIgnoreCase(request.getScheme()) && port == 80)
                || ("https".equalsIgnoreCase(request.getScheme()) && port == 443);
    }

    private String stripLeadingSlash(String value) {
        while (value.startsWith("/")) {
            value = value.substring(1);
        }

        return value.isBlank() ? "product" : value;
    }
}
