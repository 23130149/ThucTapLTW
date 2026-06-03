package service;

import jakarta.servlet.http.HttpServletRequest;
import model.Order;
import model.VnpayConfig;
import util.VnpayUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class VnpayService {

    public String createPaymentUrl(Order order, HttpServletRequest request) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expire = now.plusMinutes(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        BigDecimal totalPrice = order.getTotalPrice() == null ? BigDecimal.ZERO : order.getTotalPrice();
        long amount = totalPrice.multiply(BigDecimal.valueOf(100)).longValue();

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", VnpayConfig.VERSION);
        params.put("vnp_Command", VnpayConfig.COMMAND);
        params.put("vnp_TmnCode", VnpayConfig.TMN_CODE);
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", VnpayConfig.CURR_CODE);
        params.put("vnp_TxnRef", order.getOrderCode());
        params.put("vnp_OrderInfo", "Thanh toan don hang " + order.getOrderCode());
        params.put("vnp_OrderType", VnpayConfig.ORDER_TYPE);
        params.put("vnp_Locale", VnpayConfig.LOCALE);
        params.put("vnp_ReturnUrl", VnpayUtil.buildAbsoluteUrl(request, VnpayConfig.RETURN_PATH));
        params.put("vnp_IpAddr", VnpayUtil.getIpAddress(request));
        params.put("vnp_CreateDate", now.format(formatter));
        params.put("vnp_ExpireDate", expire.format(formatter));

        // Ép VNPAY mở luồng QR. Nếu muốn người dùng tự chọn phương thức, hãy xóa dòng này.
      //  params.put("vnp_BankCode", "VNPAYQR");

        String hashData = VnpayUtil.buildQuery(params, true);
        String secureHash = VnpayUtil.hmacSHA512(VnpayConfig.HASH_SECRET, hashData);
        String query = VnpayUtil.buildQuery(params, true);

        return VnpayConfig.PAY_URL + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    public boolean verifyReturnData(Map<String, String> params) {
        String receivedHash = params.get("vnp_SecureHash");

        if (receivedHash == null || receivedHash.trim().isEmpty()) {
            return false;
        }

        Map<String, String> verifyParams = new TreeMap<>(params);
        verifyParams.remove("vnp_SecureHash");
        verifyParams.remove("vnp_SecureHashType");

        String hashData = VnpayUtil.buildQuery(verifyParams, true);
        String calculatedHash = VnpayUtil.hmacSHA512(VnpayConfig.HASH_SECRET, hashData);

        return receivedHash.equalsIgnoreCase(calculatedHash);
    }
}
