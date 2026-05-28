package controller;

import cart.Cart;
import cart.CartItem;
import dao.CartDao;
import dao.OrderDao;
import dao.OrderItemDao;
import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.OrderItem;
import model.Product;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/Reorder")
public class ReorderController extends HttpServlet {
    private final OrderDao orderDao = new OrderDao();
    private final OrderItemDao orderItemDao = new OrderItemDao();
    private final ProductDao productDao = new ProductDao();
    private final CartDao cartDao = new CartDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.setAttribute("loginMessage", "Vui lòng đăng nhập để mua lại đơn hàng.");
            response.sendRedirect(request.getContextPath() + "/SignIn?redirect=OrderHistory");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (Exception e) {
            session.setAttribute("orderMessage", "Không tìm thấy đơn hàng cần mua lại.");
            response.sendRedirect(request.getContextPath() + "/OrderHistory");
            return;
        }

        Order order = orderDao.getOrderByIdAndUser(orderId, user.getUserId());
        if (order == null) {
            session.setAttribute("orderMessage", "Đơn hàng không thuộc tài khoản của bạn.");
            response.sendRedirect(request.getContextPath() + "/OrderHistory");
            return;
        }

        List<OrderItem> items = orderItemDao.getItemsByOrderId(orderId);
        Cart currentCart = cartDao.getCartByUserId(user.getUserId());
        int added = 0;
        int skipped = 0;

        for (OrderItem item : items) {
            Product product = productDao.getProductById(item.getProductId());
            if (product == null || product.getStockQuantity() <= 0) {
                skipped++;
                continue;
            }

            CartItem existing = currentCart.getData().get(product.getProductId());
            int existingQuantity = existing == null ? 0 : existing.getQuantity();
            int remaining = product.getStockQuantity() - existingQuantity;

            if (remaining <= 0) {
                skipped++;
                continue;
            }

            int quantityToAdd = Math.min(item.getQuantity(), remaining);
            cartDao.addProduct(user.getUserId(), product.getProductId(), quantityToAdd);
            currentCart.addProduct(product, quantityToAdd);
            added += quantityToAdd;
        }

        session.setAttribute("cart", cartDao.getCartByUserId(user.getUserId()));

        if (added > 0) {
            String message = "Đã thêm " + added + " sản phẩm từ đơn hàng cũ vào giỏ.";
            if (skipped > 0) {
                message += " Một số sản phẩm đã hết hàng hoặc vượt tồn kho nên không thêm.";
            }
            session.setAttribute("cartMessage", message);
            response.sendRedirect(request.getContextPath() + "/cart");
        } else {
            session.setAttribute("orderMessage", "Không thể mua lại vì các sản phẩm trong đơn đã hết hàng hoặc vượt tồn kho.");
            response.sendRedirect(request.getContextPath() + "/OrderHistory");
        }
    }
}
