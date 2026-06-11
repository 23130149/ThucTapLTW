package model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Contact {
    private int contactId;
    private String contactName;
    private String contactEmail;
    private String phone;
    private String subject;
    private String message;
    private Integer userId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String status;
    private String reply;
    private LocalDateTime replyAt;

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getCreateAtFormatted() {
        if (createAt == null) return "Chưa cập nhật";
        return createAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    public String getStatus() {
        return status == null || status.isBlank() ? "NEW" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return switch (getStatus()) {
            case "PROCESSING" -> "Đang xử lý";
            case "DONE" -> "Đã xử lý";
            default -> "Chưa xử lý";
        };
    }

    public String getStatusClass() {
        return switch (getStatus()) {
            case "PROCESSING" -> "processing";
            case "DONE" -> "done";
            default -> "new";
        };
    }
    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public LocalDateTime getReplyAt() {
        return replyAt;
    }

    public void setReplyAt(LocalDateTime replyAt) {
        this.replyAt = replyAt;
    }

    public String getReplyAtFormatted() {
        if (replyAt == null) return "Chưa phản hồi";
        return replyAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}


