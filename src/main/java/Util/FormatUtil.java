package util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class    FormatUtil {

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
        switch (status) {
            case "PENDING": return "Chờ xác nhận";
            case "CONFIRMED": return "Đã xác nhận";
            case "SHIPPED": return "Đang giao";
            case "COMPLETED": return "Hoàn thành";
            case "CANCELLED": return "Đã huỷ";
            default: return status;
        }
    }

    public static String orderStatusIcon(String status) {
        if (status == null) return "";
        switch (status) {
            case "COMPLETED": return "✔";
            case "SHIPPED": return "⏳";
            case "CANCELLED": return "✖";
            default: return "⏳";
        }
    }
}
