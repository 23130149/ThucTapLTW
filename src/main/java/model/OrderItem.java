package model;

import java.math.BigDecimal;
import util.FormatUtil;

public class OrderItem {

    private int orderItemId;
    private int orderId;
    private int productId;

    private String productName;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal totalPrice;

    public String getUnitPriceFormatted() {
        return FormatUtil.formatMoney(unitPrice);
    }

    public String getTotalPriceFormatted() {
        return FormatUtil.formatMoney(totalPrice);
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}

