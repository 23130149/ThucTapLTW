package model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class User {

    private int userId;
    private String userName;
    private String email;
    private String phone;
    private String password;
    private LocalDateTime createAt;
    private String role;
    private String googleId;
    private int orderCount;
    private BigDecimal totalSpend;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(BigDecimal totalSpend) {
        this.totalSpend = totalSpend;
    }
    public String getRole() {
        return role;
    }
    public String getTotalSpendFormatted() {
        if (totalSpend == null) return "0 ₫";
        NumberFormat vn = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return vn.format(totalSpend);
    }

    public String getJoinDateFormatted() {
        if (createAt == null) return "";
        return createAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCustomerType() {
        if (totalSpend.doubleValue() >= 10000000) return "vip";
        if (totalSpend.doubleValue() >= 3000000) return "regular";
        return "new";
    }


    public String getCustomerTypeLabel() {
        switch (getCustomerType()) {
            case "vip": return "Vip";
            case "regular": return "Thường xuyên";
            default: return "Mới";
        }
    }
}