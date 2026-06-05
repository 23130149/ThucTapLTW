package controller.payment;

import dao.CartDao;
import dao.OrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.VnpayService;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "VnpayReturnController", value = "/vnpay-return")
public class VnpayReturnController extends HttpServlet {

    private final VnpayService vnpayService = new VnpayService();
    private final OrderDao orderDao = new OrderDao();
    private final CartDao cartDao = new CartDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> params = extractParams(request);
        boolean validSignature = vnpayService.verifyReturnData(params);

        String responseCode = request.getParameter("vnp_ResponseCode");
        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        String orderCode = request.getParameter("vnp_TxnRef");
        String transactionNo = request.getParameter("vnp_TransactionNo");

        HttpSession session = request.getSession(false);

        if (validSignature && "00".equals(responseCode) && "00".equals(transactionStatus)) {
            orderDao.markVnpayPaid(orderCode, transactionNo, responseCode);
            removePaidProductsFromCart(session, orderCode);

            if (session != null) {
                session.removeAttribute("checkoutProductIds");
                session.removeAttribute("pendingVnpayOrderCode");
                session.removeAttribute("pendingVnpayProductIds");
                session.setAttribute("orderSuccess", "Thanh toán VNPAY thành công!");
            }

            response.sendRedirect(request.getContextPath() + "/OrderHistory");
            return;
        }

        orderDao.markVnpayFailed(orderCode, responseCode == null ? "INVALID" : responseCode);

        if (session != null) {
            session.setAttribute("paymentError", "Thanh toán VNPAY không thành công hoặc dữ liệu không hợp lệ.");
        }

        response.sendRedirect(request.getContextPath() + "/payment");
    }

    private Map<String, String> extractParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        });

        return params;
    }

    private void removePaidProductsFromCart(HttpSession session, String orderCode) {
        if (session == null) {
            return;
        }

        Object pendingOrderCode = session.getAttribute("pendingVnpayOrderCode");
        if (pendingOrderCode == null || !pendingOrderCode.toString().equals(orderCode)) {
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return;
        }

        Set<Integer> productIds = new HashSet<>();
        Object rawIds = session.getAttribute("pendingVnpayProductIds");

        if (rawIds instanceof Set<?>) {
            for (Object rawId : (Set<?>) rawIds) {
                if (rawId instanceof Integer) {
                    productIds.add((Integer) rawId);
                }
            }
        }

        if (!productIds.isEmpty()) {
            cartDao.removeProducts(user.getUserId(), productIds);
            session.setAttribute("cart", cartDao.getCartByUserId(user.getUserId()));
        }
    }
}
