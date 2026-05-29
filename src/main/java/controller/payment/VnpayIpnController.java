package controller.payment;

import dao.CartDao;
import dao.OrderDao;
import dao.OrderItemDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Order;
import model.OrderItem;
import service.VnpayService;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "VnpayIpnController", value = "/vnpay-ipn")
public class VnpayIpnController extends HttpServlet {

    private final VnpayService vnpayService = new VnpayService();
    private final OrderDao orderDao = new OrderDao();
    private final OrderItemDao orderItemDao = new OrderItemDao();
    private final CartDao cartDao = new CartDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processIpn(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processIpn(request, response);
    }

    private void processIpn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        Map<String, String> params = extractParams(request);
        boolean validSignature = vnpayService.verifyReturnData(params);

        if (!validSignature) {
            response.getWriter().write("{\"RspCode\":\"97\",\"Message\":\"Invalid signature\"}");
            return;
        }

        String orderCode = request.getParameter("vnp_TxnRef");
        String responseCode = request.getParameter("vnp_ResponseCode");
        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        String transactionNo = request.getParameter("vnp_TransactionNo");

        Order order = orderDao.getOrderByCode(orderCode);
        if (order == null) {
            response.getWriter().write("{\"RspCode\":\"01\",\"Message\":\"Order not found\"}");
            return;
        }

        if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
            boolean updated = orderDao.markVnpayPaid(orderCode, transactionNo, responseCode);

            if (updated) {
                removeOrderItemsFromCart(order);
            }

            response.getWriter().write("{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}");
            return;
        }

        orderDao.markVnpayFailed(orderCode, responseCode == null ? "INVALID" : responseCode);
        response.getWriter().write("{\"RspCode\":\"00\",\"Message\":\"Confirm Failed Payment\"}");
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

    private void removeOrderItemsFromCart(Order order) {
        if (order == null || order.getUserId() <= 0 || order.getOrderId() <= 0) {
            return;
        }

        List<OrderItem> items = orderItemDao.getItemsByOrderId(order.getOrderId());
        Set<Integer> productIds = new HashSet<>();

        for (OrderItem item : items) {
            productIds.add(item.getProductId());
        }

        if (!productIds.isEmpty()) {
            cartDao.removeProducts(order.getUserId(), productIds);
        }
    }
}
