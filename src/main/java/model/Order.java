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
    private String paymentProvider;
    private String paymentTransactionNo;
    private String paymentResponseCode;
    private LocalDateTime paidAt;
    private int paymentMethodId;
    private String paymentStatus;
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
        if (createAt == null) return "Đang cập nhật";
        return createAt.plusDays(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getDeliveredAtFormatted() {
        if (createAt == null) return "Đang cập nhật";
        if ("COMPLETED".equals(status) || "RETURN_REQUESTED".equals(status) || "RETURNED".equals(status)) {
            return createAt.plusDays(4).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "Chưa giao";
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
