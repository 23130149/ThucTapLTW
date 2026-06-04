package model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Order {

    private int orderId;
    private int userId;
    private int userAddressId;
    private LocalDateTime createAt;
    private String status;
    private String orderCode;
    private String note;
    private BigDecimal totalPrice;
    private String userName;
    private String productName;
    private int quantity;
    private int totalQuantity;
    private String shipAddress;
    private String shipName;
    private String shipPhone;
    private int paymentMethodId;
    private String paymentStatus;
    private String paymentProvider;
    private String paymentTransactionNo;
    private String paymentResponseCode;
    private LocalDateTime paidAt;
    private String ghnOrderCode;
    private String ghnStatus;
    private LocalDateTime ghnUpdatedAt;
    private LocalDateTime ghnLeadtime;
    private LocalDateTime ghnFinishDate;

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipPhone() {
        return shipPhone;
    }

    public void setShipPhone(String shipPhone) {
        this.shipPhone = shipPhone;
    }

    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public String getPaymentTransactionNo() {
        return paymentTransactionNo;
    }

    public void setPaymentTransactionNo(String paymentTransactionNo) {
        this.paymentTransactionNo = paymentTransactionNo;
    }

    public String getPaymentResponseCode() {
        return paymentResponseCode;
    }

    public void setPaymentResponseCode(String paymentResponseCode) {
        this.paymentResponseCode = paymentResponseCode;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public String getGhnOrderCode() {
        return ghnOrderCode;
    }

    public void setGhnOrderCode(String ghnOrderCode) {
        this.ghnOrderCode = ghnOrderCode;
    }

    public String getGhnStatus() {
        return ghnStatus;
    }

    public void setGhnStatus(String ghnStatus) {
        this.ghnStatus = ghnStatus;
    }

    public LocalDateTime getGhnUpdatedAt() {
        return ghnUpdatedAt;
    }

    public void setGhnUpdatedAt(LocalDateTime ghnUpdatedAt) {
        this.ghnUpdatedAt = ghnUpdatedAt;
    }

    public LocalDateTime getGhnLeadtime() {
        return ghnLeadtime;
    }

    public void setGhnLeadtime(LocalDateTime ghnLeadtime) {
        this.ghnLeadtime = ghnLeadtime;
    }

    public LocalDateTime getGhnFinishDate() {
        return ghnFinishDate;
    }

    public void setGhnFinishDate(LocalDateTime ghnFinishDate) {
        this.ghnFinishDate = ghnFinishDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getTotalQuantity(){return  totalQuantity;};
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getShipAddress() {
        return shipAddress;
    }
    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }
    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public int getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(int userAddressId) {
        this.userAddressId = userAddressId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreateAtFormatted() {
        if (createAt == null) return "";
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return createAt.format(formatter);
    }
    public String getTotalPriceFormatted() {
        if (totalPrice == null) return "0 ₫";
        NumberFormat vn =
                NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return vn.format(totalPrice);
    }

    public String getEstimatedDeliveryFormatted() {
        if (ghnLeadtime != null) {
            return ghnLeadtime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        if (createAt == null) return "Đang cập nhật";
        return createAt.plusDays(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getDeliveredAtFormatted() {
        if (ghnFinishDate != null) {
            return ghnFinishDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        if (createAt == null) return "Đang cập nhật";
        if ("DELIVERED".equals(status) || "COMPLETED".equals(status) || "RETURN_REQUESTED".equals(status) || "RETURNED".equals(status)) {
            return createAt.plusDays(4).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "Chưa giao";
    }

    public String getGhnStatusLabel() {
        if (ghnStatus == null || ghnStatus.isBlank()) {
            return "Chưa tạo vận đơn";
        }
        return switch (ghnStatus) {
            case "ready_to_pick" -> "Đã tạo vận đơn, chờ GHN lấy hàng";
            case "picking", "money_collect_picking" -> "GHN đang đến lấy hàng";
            case "picked" -> "GHN đã nhận hàng";
            case "storing", "sorting", "transporting" -> "GHN đang trung chuyển";
            case "delivering", "money_collect_delivering" -> "GHN đang giao tới khách";
            case "delivered" -> "GHN đã giao tới khách";
            case "delivery_fail" -> "GHN giao hàng chưa thành công";
            case "waiting_to_return" -> "GHN đang chờ xử lý giao lại";
            case "return", "return_transporting", "return_sorting", "returning" -> "GHN đang hoàn hàng";
            case "returned" -> "GHN đã hoàn hàng về cửa hàng";
            case "return_fail" -> "GHN hoàn hàng chưa thành công";
            case "cancel" -> "Vận đơn GHN đã hủy";
            case "damage" -> "Hàng hóa bị hư hỏng";
            case "lost" -> "Hàng hóa bị thất lạc";
            case "exception" -> "GHN đang xử lý ngoại lệ";
            default -> ghnStatus;
        };
    }

    public String getGhnUpdatedAtFormatted() {
        if (ghnUpdatedAt == null) {
            return "";
        }
        return ghnUpdatedAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public boolean isGhnDelivered() {
        return "delivered".equals(ghnStatus);
    }

    public boolean isCancellable() {
        return "PENDING".equals(status) || "PROCESSING".equals(status) || "CONFIRMED".equals(status);
    }

    public boolean isReturnable() {
        return "COMPLETED".equals(status);
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
