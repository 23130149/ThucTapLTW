package controller.cart;

import cart.Cart;
import dao.CartDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@WebServlet(name = "DelSelectProduct", value = "/DelSelectProduct")
public class DelSelectProduct extends HttpServlet {
    private final CartDao cartDao = new CartDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("loginMessage", "Vui lòng đăng nhập để xóa sản phẩm trong giỏ hàng.");
            session.setAttribute("redirectAfterLogin", "cart");
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        String[] ids = request.getParameterValues("productIds");
        Set<Integer> productIds = new LinkedHashSet<>();

        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                try {
                    int productId = Integer.parseInt(id.trim());
                    if (productId > 0) {
                        productIds.add(productId);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        int deletedCount = cartDao.removeProducts(user.getUserId(), productIds);

        if (deletedCount > 0) {
            session.setAttribute("toastMessage", "Đã xóa " + deletedCount + " sản phẩm");
            session.setAttribute("toastType", "hh-toast-cart");
            session.setAttribute("toastIcon", "bx-trash");
        } else {
            session.setAttribute("cartError", "Vui lòng chọn sản phẩm cần xóa");
        }

        Cart cart = cartDao.getCartByUserId(user.getUserId());
        session.setAttribute("cart", cart);
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
