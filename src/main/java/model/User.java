package model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.time.LocalDate;

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
    private String customerCode;
    private LocalDate dateOfBirth;
    private String gender;
    private String avatarUrl;
    private String bio;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDateOfBirthFormatted() {
        if (dateOfBirth == null) return "Chưa cập nhật";
        return dateOfBirth.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getGenderLabel() {
        if (gender == null || gender.isBlank()) return "Chưa cập nhật";
        switch (gender) {
            case "MALE": return "Nam";
            case "FEMALE": return "Nữ";
            case "OTHER": return "Khác";
            default: return gender;
        }
    }

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
        if (totalSpend == null) return "new";
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