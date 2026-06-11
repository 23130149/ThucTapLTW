package util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatUtil {

    private static final DateTimeFormatter DATE_TIME_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String formatDateTime(LocalDateTime dt) {
        if (dt == null) return "";
        return dt.format(DATE_TIME_FMT);
    }

    public static String formatMoney(BigDecimal money) {
        if (money == null) return "0 đ";
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(money) + " đ";
    }

    public static String orderStatusLabel(String status) {
        if (status == null) return "";
        return switch (status) {
            case "PENDING" -> "Chờ xác nhận";
            case "PENDING_PAYMENT" -> "Chờ thanh toán";
            case "PAYMENT_FAILED" -> "Thanh toán lỗi";
            case "PROCESSING" -> "Đang xử lý";
            case "CONFIRMED" -> "Đã xác nhận";
            case "SHIPPED" -> "Đang giao";
            case "DELIVERED" -> "Chờ xác nhận";
            case "COMPLETED" -> "Đã nhận hàng";
            case "CANCELLED" -> "Đã hủy";
            case "RETURN_REQUESTED" -> "Đang yêu cầu trả hàng";
            case "RETURNED" -> "Đã trả hàng";
            case "RETURN_REJECTED" -> "Từ chối trả hàng";
            default -> status;
        };
    }

    public static String orderStatusIcon(String status) {
        if (status == null) return "";
        return switch (status) {
            case "COMPLETED" -> "✓";
            case "DELIVERED" -> "";
            case "SHIPPED" -> "🚚";
            case "CANCELLED" -> "✖";
            case "RETURN_REQUESTED", "RETURNED" -> "↩";
            case "RETURN_REJECTED" -> "!";
            default -> "⏳";
        };
    }

    public static String ghnStatusLabel(String status) {
        if (status == null || status.isBlank()) return "Chưa tạo vận đơn";
        return switch (status) {
            case "ready_to_pick" -> "Chờ GHN lấy hàng";
            case "picking", "money_collect_picking" -> "GHN đang đến lấy hàng";
            case "picked" -> "GHN đã nhận hàng";
            case "storing", "sorting", "transporting" -> "GHN đang trung chuyển";
            case "delivering", "money_collect_delivering" -> "GHN đang giao tới khách";
            case "delivered" -> "GHN đã giao tới khách";
            case "delivery_fail" -> "GHN giao hàng chưa thành công";
            case "waiting_to_return" -> "GHN đang chờ hoàn hàng";
            case "return", "return_transporting", "return_sorting", "returning" -> "GHN đang hoàn hàng";
            case "returned" -> "GHN đã hoàn hàng";
            case "return_fail" -> "GHN hoàn hàng chưa thành công";
            case "cancel" -> "Vận đơn GHN đã hủy";
            case "damage" -> "Hàng hóa bị hư hỏng";
            case "lost" -> "Hàng hóa bị thất lạc";
            case "exception" -> "GHN đang xử lý ngoại lệ";
            default -> status;
        };
    }
}
