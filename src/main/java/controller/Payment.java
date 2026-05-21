package controller;

import cart.Cart;
import dao.OrderDao;
import dao.OrderItemDao;
import dao.UserAddressDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Order;
import model.User;
import model.UserAddress;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@WebServlet(name = "Payment", value = "/payment")
public class Payment extends HttpServlet {

    private final UserAddressDao addressDao = new UserAddressDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn?redirect=payment");
            return;
        }

        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getList() == null || cart.getList().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        List<UserAddress> addresses = addressDao.findByUserId(user.getUserId());

        UserAddress defaultAddress = null;
        if (addresses != null && !addresses.isEmpty()) {
            defaultAddress = addresses.get(0);
        }

        BigDecimal totalPrice = BigDecimal.valueOf(cart.getTotalPrice());
        BigDecimal shippingFee = BigDecimal.ZERO;

        if (defaultAddress != null) {
            shippingFee = calculateShippingFee(defaultAddress);
        }

        BigDecimal grandTotal = totalPrice.add(shippingFee);

        request.setAttribute("addresses", addresses);
        request.setAttribute("address", defaultAddress);
        request.setAttribute("cartItems", cart.getList());
        request.setAttribute("totalPrice", totalPrice);
        request.setAttribute("shippingFee", shippingFee);
        request.setAttribute("grandTotal", grandTotal);

        request.getRequestDispatcher("/payment.jsp").forward(request, response);
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
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getList() == null || cart.getList().isEmpty()) {
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

        BigDecimal totalPrice = BigDecimal.valueOf(cart.getTotalPrice());
        BigDecimal shippingFee = calculateShippingFee(addr);
        BigDecimal grandTotal = totalPrice.add(shippingFee);

        String shipAddress = buildShipAddress(addr);

        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setUserAddressId(addressId);
        order.setNote(request.getParameter("note"));
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

        cart.getList().forEach(item -> orderItemDao.insert(orderId, item));

        session.removeAttribute("cart");
        session.setAttribute("orderSuccess", "Đặt hàng thành công!");

        response.sendRedirect(request.getContextPath() + "/OrderHistory");
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