package controller;

import cart.Cart;
import cart.CartItem;
import dao.CartDao;
import dao.OrderDao;
import dao.OrderItemDao;
import dao.UserAddressDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Order;
import model.User;
import model.UserAddress;
import service.GhnService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "Payment", value = "/payment")
public class Payment extends HttpServlet {

    private final UserAddressDao addressDao = new UserAddressDao();
    private final CartDao cartDao = new CartDao();
    private final GhnService ghnService = new GhnService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn?redirect=payment");
            return;
        }

        User user = (User) session.getAttribute("user");
        Cart cart = cartDao.getCartByUserId(user.getUserId());
        session.setAttribute("cart", cart);

        if (cart == null || cart.getList() == null || cart.getList().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        List<CartItem> selectedItems = getSelectedCartItems(cart, request.getParameterValues("productIds"));

        if (selectedItems.isEmpty()) {
            selectedItems = getSelectedCartItemsFromSession(cart, session);
        }

        if (selectedItems.isEmpty()) {
            session.setAttribute("cartError", "Vui lòng chọn ít nhất một sản phẩm để thanh toán.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        session.setAttribute("checkoutProductIds", getSelectedProductIdSet(selectedItems));

        List<UserAddress> addresses = addressDao.findByUserId(user.getUserId());

        UserAddress defaultAddress = null;
        if (addresses != null && !addresses.isEmpty()) {
            defaultAddress = addresses.get(0);
        }

        BigDecimal totalPrice = calculateTotalPrice(selectedItems);
        BigDecimal shippingFee = BigDecimal.ZERO;

        if (defaultAddress != null) {
            shippingFee = calculateShippingFee(defaultAddress);
        }

        BigDecimal grandTotal = totalPrice.add(shippingFee);

        request.setAttribute("addresses", addresses);
        request.setAttribute("address", defaultAddress);
        request.setAttribute("cartItems", selectedItems);
        request.setAttribute("totalPrice", totalPrice);
        request.setAttribute("shippingFee", shippingFee);
        request.setAttribute("grandTotal", grandTotal);

        request.getRequestDispatcher("/jsp/payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn?redirect=payment");
            return;
        }

        User user = (User) session.getAttribute("user");
        Cart cart = cartDao.getCartByUserId(user.getUserId());
        session.setAttribute("cart", cart);

        if (cart == null || cart.getList() == null || cart.getList().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        List<CartItem> selectedItems = getSelectedCartItems(cart, request.getParameterValues("productIds"));

        if (selectedItems.isEmpty()) {
            selectedItems = getSelectedCartItemsFromSession(cart, session);
        }

        if (selectedItems.isEmpty()) {
            session.setAttribute("cartError", "Vui lòng chọn sản phẩm cần thanh toán.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        String addressIdRaw = request.getParameter("addressId");

        if (addressIdRaw == null || addressIdRaw.trim().isEmpty()) {
            session.setAttribute("paymentError", "Vui lòng chọn địa chỉ giao hàng.");
            response.sendRedirect(request.getContextPath() + "/payment");
            return;
        }

        int addressId;

        try {
            addressId = Integer.parseInt(addressIdRaw.trim());
        } catch (NumberFormatException e) {
            session.setAttribute("paymentError", "Địa chỉ giao hàng không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/payment");
            return;
        }

        UserAddress addr = addressDao.findById(addressId);

        if (addr == null || addr.getUserId() != user.getUserId()) {
            session.setAttribute("paymentError", "Địa chỉ giao hàng không tồn tại hoặc không thuộc tài khoản của bạn.");
            response.sendRedirect(request.getContextPath() + "/payment");
            return;
        }

        BigDecimal totalPrice = calculateTotalPrice(selectedItems);
        BigDecimal shippingFee = calculateShippingFee(addr);
        BigDecimal grandTotal = totalPrice.add(shippingFee);

        String shipAddress = buildShipAddress(addr);

        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setUserAddressId(addressId);
        order.setNote(getOrderNote(request));
        order.setStatus("PENDING");
        order.setOrderCode("DH" + System.currentTimeMillis());
        order.setTotalPrice(grandTotal);
        order.setShipAddress(shipAddress);

        OrderDao orderDao = new OrderDao();
        int orderId = orderDao.insertAndReturnId(order);

        if (orderId <= 0) {
            session.setAttribute("paymentError", "Không thể tạo đơn hàng. Vui lòng thử lại.");
            response.sendRedirect(request.getContextPath() + "/payment");
            return;
        }

        OrderItemDao orderItemDao = new OrderItemDao();

        Set<Integer> paidProductIds = new HashSet<>();

        for (CartItem item : selectedItems) {
            orderItemDao.insert(orderId, item);
            paidProductIds.add(item.getProduct().getProductId());
        }

        cartDao.removeProducts(user.getUserId(), paidProductIds);
        session.removeAttribute("checkoutProductIds");
        session.setAttribute("cart", cartDao.getCartByUserId(user.getUserId()));

        session.setAttribute("orderSuccess", "Đặt hàng thành công!");

        response.sendRedirect(request.getContextPath() + "/OrderHistory");
    }

    private List<CartItem> getSelectedCartItems(Cart cart, String[] productIdParams) {
        List<CartItem> selectedItems = new ArrayList<>();

        if (cart == null || productIdParams == null || productIdParams.length == 0) {
            return selectedItems;
        }

        Set<Integer> selectedIds = new HashSet<>();

        for (String productIdRaw : productIdParams) {
            if (productIdRaw == null || productIdRaw.trim().isEmpty()) {
                continue;
            }

            try {
                int productId = Integer.parseInt(productIdRaw.trim());
                if (productId > 0) {
                    selectedIds.add(productId);
                }
            } catch (NumberFormatException ignored) {
            }
        }

        for (Integer productId : selectedIds) {
            CartItem item = cart.getItem(productId);
            if (item != null) {
                selectedItems.add(item);
            }
        }

        return selectedItems;
    }

    private List<CartItem> getSelectedCartItemsFromSession(Cart cart, HttpSession session) {
        Object value = session.getAttribute("checkoutProductIds");

        if (!(value instanceof Set<?>)) {
            return new ArrayList<>();
        }

        Set<Integer> productIds = new HashSet<>();

        for (Object item : (Set<?>) value) {
            if (item instanceof Integer) {
                productIds.add((Integer) item);
            }
        }

        List<CartItem> selectedItems = new ArrayList<>();

        for (Integer productId : productIds) {
            CartItem cartItem = cart.getItem(productId);
            if (cartItem != null) {
                selectedItems.add(cartItem);
            }
        }

        return selectedItems;
    }

    private Set<Integer> getSelectedProductIdSet(List<CartItem> selectedItems) {
        Set<Integer> selectedIds = new HashSet<>();

        for (CartItem item : selectedItems) {
            selectedIds.add(item.getProduct().getProductId());
        }

        return selectedIds;
    }

    private BigDecimal calculateTotalPrice(List<CartItem> items) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : items) {
            BigDecimal price = item.getPrice() == null ? BigDecimal.ZERO : item.getPrice();
            total = total.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        return total;
    }

    private String getOrderNote(HttpServletRequest request) {
        String orderNote = request.getParameter("orderNote");

        if (orderNote != null && !orderNote.trim().isEmpty()) {
            return orderNote.trim();
        }

        String note = request.getParameter("note");
        return note == null ? null : note.trim();
    }

    private String buildShipAddress(UserAddress addr) {
        return String.join(", ",
                safe(addr.getStreet()),
                safe(addr.getDistrict()),
                safe(addr.getProvince()),
                safe(addr.getCountry())
        );
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private BigDecimal calculateShippingFee(UserAddress address) {
        if (address != null && address.getDistrictId() != null && address.getWardCode() != null && !address.getWardCode().isBlank()) {
            try {
                return ghnService.calculateFee(address.getDistrictId(), address.getWardCode());
            } catch (IOException | InterruptedException e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        String shipAddress = buildShipAddress(address);
        double distanceKm = estimateDistanceKm(shipAddress);
        return calculateFeeByDistance(distanceKm);
    }

    private BigDecimal calculateFeeByDistance(double distanceKm) {
        if (distanceKm <= 0) {
            return BigDecimal.valueOf(30000);
        }

        BigDecimal baseFee = BigDecimal.valueOf(20000);
        BigDecimal feePerKm = BigDecimal.valueOf(4000);

        if (distanceKm <= 5) {
            return baseFee;
        }

        BigDecimal extraDistance = BigDecimal.valueOf(distanceKm - 5);
        BigDecimal extraFee = extraDistance.multiply(feePerKm);

        return baseFee.add(extraFee).setScale(0, RoundingMode.HALF_UP);
    }

    private double estimateDistanceKm(String shipAddress) {
        String address = shipAddress.toLowerCase();

        if (address.contains("thủ đức") || address.contains("quận 1") || address.contains("quận 3")) {
            return 5;
        }

        if (address.contains("hồ chí minh") || address.contains("tp hcm") || address.contains("tphcm")) {
            return 10;
        }

        if (address.contains("bình dương") || address.contains("đồng nai")) {
            return 25;
        }

        return 15;
    }
}
