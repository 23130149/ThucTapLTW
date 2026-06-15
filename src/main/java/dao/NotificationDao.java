package dao;

import java.util.List;
import java.util.Map;
import java.util.Locale;

public class NotificationDao extends BaseDao {

    private void ensureTable() {
        try {
            getJdbi().useHandle(handle -> handle.execute("""
                    CREATE TABLE IF NOT EXISTS notifications (
                        Notification_Id INT NOT NULL AUTO_INCREMENT,
                        User_Id INT NOT NULL,
                        Type VARCHAR(40) NOT NULL,
                        Title VARCHAR(255) NOT NULL,
                        Message TEXT NULL,
                        Target_Url VARCHAR(600) NULL,
                        Source_Type VARCHAR(40) NULL,
                        Source_Id INT NULL,
                        Is_Read TINYINT(1) NOT NULL DEFAULT 0,
                        Create_At DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        Read_At DATETIME NULL,
                        PRIMARY KEY (Notification_Id),
                        INDEX idx_notifications_user_read (User_Id, Is_Read, Create_At),
                        INDEX idx_notifications_user_date (User_Id, Create_At),
                        INDEX idx_notifications_source (User_Id, Source_Type, Source_Id, Type)
                    ) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
                    """));
        } catch (Exception ignored) {
        }
        tryAddColumn("ALTER TABLE notifications ADD COLUMN Source_Type VARCHAR(40) NULL");
        tryAddColumn("ALTER TABLE notifications ADD COLUMN Source_Id INT NULL");
        tryAddColumn("ALTER TABLE notifications ADD COLUMN Read_At DATETIME NULL");
        tryAddIndex("CREATE INDEX idx_notifications_user_date ON notifications (User_Id, Create_At)");
        tryAddIndex("CREATE INDEX idx_notifications_source ON notifications (User_Id, Source_Type, Source_Id, Type)");
    }

    private void tryAddColumn(String sql) {
        try {
            getJdbi().useHandle(handle -> handle.execute(sql));
        } catch (Exception ignored) {
        }
    }

    private void tryAddIndex(String sql) {
        try {
            getJdbi().useHandle(handle -> handle.execute(sql));
        } catch (Exception ignored) {
        }
    }

    public Integer findUserIdByEmail(String email) {
        ensureTable();
        if (email == null || email.isBlank()) {
            return null;
        }
        try {
            return getJdbi().withHandle(handle -> handle.createQuery("""
                            SELECT User_Id
                            FROM `user`
                            WHERE LOWER(Email) = LOWER(:email)
                            LIMIT 1
                            """)
                    .bind("email", email.trim())
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null));
        } catch (Exception ignored) {
            return null;
        }
    }

    public void addSafe(int userId, String type, String title, String message, String targetUrl) {
        try {
            add(userId, type, title, message, targetUrl);
        } catch (Exception ignored) {
        }
    }

    public void addOrRefreshSafe(int userId, String type, String title, String message,
                                 String targetUrl, String sourceType, Integer sourceId) {
        try {
            addOrRefresh(userId, type, title, message, targetUrl, sourceType, sourceId);
        } catch (Exception ignored) {
        }
    }

    public void add(int userId, String type, String title, String message, String targetUrl) {
        addOrRefresh(userId, type, title, message, targetUrl, null, null);
    }

    public void addOrRefresh(int userId, String type, String title, String message,
                             String targetUrl, String sourceType, Integer sourceId) {
        ensureTable();
        if (userId <= 0 || type == null || type.isBlank() || title == null || title.isBlank()) {
            return;
        }

        String normalizedType = type.trim().toUpperCase();
        String normalizedSourceType = sourceType == null || sourceType.isBlank() ? null : sourceType.trim().toUpperCase();

        String[] customerText = normalizeCustomerText(normalizedType, title.trim(), message, sourceId);
        String displayTitle = customerText[0];
        String displayMessage = customerText[1];

        if (normalizedSourceType != null && sourceId != null && sourceId > 0) {
            int updated = getJdbi().withHandle(handle -> handle.createUpdate("""
                            UPDATE notifications
                            SET Title = :title,
                                Message = :message,
                                Target_Url = :targetUrl,
                                Is_Read = 0,
                                Read_At = NULL,
                                Create_At = NOW()
                            WHERE User_Id = :userId
                              AND Type = :type
                              AND Source_Type = :sourceType
                              AND Source_Id = :sourceId
                            """)
                    .bind("title", displayTitle)
                    .bind("message", displayMessage)
                    .bind("targetUrl", targetUrl)
                    .bind("userId", userId)
                    .bind("type", normalizedType)
                    .bind("sourceType", normalizedSourceType)
                    .bind("sourceId", sourceId)
                    .execute());

            if (updated > 0) {
                return;
            }
        }

        getJdbi().useHandle(handle -> handle.createUpdate("""
                        INSERT INTO notifications
                            (User_Id, Type, Title, Message, Target_Url, Source_Type, Source_Id, Is_Read, Create_At)
                        VALUES
                            (:userId, :type, :title, :message, :targetUrl, :sourceType, :sourceId, 0, NOW())
                        """)
                .bind("userId", userId)
                .bind("type", normalizedType)
                .bind("title", displayTitle)
                .bind("message", displayMessage)
                .bind("targetUrl", targetUrl)
                .bind("sourceType", normalizedSourceType)
                .bind("sourceId", sourceId)
                .execute());
    }

    private String cleanText(String value) {
        return value == null ? "" : value.replaceAll("\\s+", " ").trim();
    }

    private String afterColon(String value) {
        String text = cleanText(value);
        int index = text.indexOf(':');
        return index >= 0 ? cleanText(text.substring(index + 1)) : text;
    }

    private String buildOrderStatusTitle(String status) {
        String lower = cleanText(status).toLowerCase(Locale.ROOT);
        if (lower.contains("xác nhận")) return "Handmade House đã xác nhận đơn hàng";
        if (lower.contains("xử lý")) return "Handmade House đang xử lý đơn hàng";
        if (lower.contains("giao")) return "Đơn hàng đang được giao đến bạn";
        if (lower.contains("hoàn thành")) return "Đơn hàng đã hoàn thành";
        if (lower.contains("hủy") || lower.contains("huỷ")) return "Đơn hàng đã bị hủy";
        return "Handmade House đã cập nhật đơn hàng";
    }

    private String cleanGhnMessage(String message) {
        String text = cleanText(message);
        if (text.isBlank()) {
            return "GHN đã cập nhật trạng thái giao hàng cho đơn hàng của bạn.";
        }
        text = text.replaceFirst("(?i)^GHN\\s*:\\s*", "");
        text = text.replaceFirst("(?i)^GHN\\s+", "");
        text = text.replaceAll("(?iu)giao\\s+(tới|đến|cho)\\s+khách hàng", "giao đơn hàng đến bạn");
        text = text.replaceAll("(?iu)giao\\s+(tới|đến|cho)\\s+khách", "giao đơn hàng đến bạn");
        text = text.replaceAll("(?iu)cho\\s+khách hàng", "cho bạn");
        text = text.replaceAll("(?iu)cho\\s+khách", "cho bạn");
        text = text.replaceAll("(?iu)khách hàng", "bạn");
        text = text.replaceAll("(?iu)\\bkhách\\b", "bạn");
        String lower = text.toLowerCase(Locale.ROOT);
        if (lower.contains("đang giao") && !lower.contains("bạn")) {
            text = "đang giao đơn hàng đến bạn";
        }
        lower = text.toLowerCase(Locale.ROOT);
        if ((lower.contains("giao") || lower.contains("vận chuyển") || lower.contains("ship"))
                && !lower.startsWith("ghn")) {
            text = "GHN " + text;
        }
        return text.isBlank() ? "GHN đã cập nhật trạng thái giao hàng cho đơn hàng của bạn." : text;
    }

    private String[] normalizeCustomerText(String type, String title, String message, Integer sourceId) {
        String displayTitle = cleanText(title);
        String displayMessage = cleanText(message);
        String orderText = sourceId != null && sourceId > 0 ? "Đơn hàng #" + sourceId + ". " : "";

        switch (type) {
            case "ORDER_STATUS" -> {
                String status = afterColon(!displayMessage.isBlank() ? displayMessage : displayTitle);
                displayTitle = buildOrderStatusTitle(status);
                displayMessage = orderText
                        + (!status.isBlank() ? "Handmade House đã chuyển trạng thái đơn hàng sang: " + status + ". " : "")
                        + "Nhấn để xem chi tiết đơn hàng.";
            }
            case "ORDER_CREATED" -> {
                displayTitle = "Handmade House đã nhận đơn hàng của bạn";
                displayMessage = (displayMessage.isBlank() ? orderText + "Đơn hàng mới đã được tạo." : displayMessage)
                        + " Handmade House sẽ sớm xác nhận và chuẩn bị đơn hàng cho bạn.";
            }
            case "ORDER_SHIPPING" -> {
                String shipping = cleanGhnMessage(displayMessage);
                displayTitle = shipping.toLowerCase(Locale.ROOT).contains("đang giao")
                        ? "GHN đang giao đơn hàng đến bạn"
                        : "GHN đã cập nhật giao hàng";
                displayMessage = orderText + shipping + ". Nhấn để xem chi tiết giao hàng.";
            }
            case "PAYMENT", "PAYMENT_SUCCESS" -> {
                displayTitle = "Handmade House đã cập nhật thanh toán của bạn";
                if (displayMessage.isBlank()) {
                    displayMessage = orderText + "Nhấn để xem chi tiết thanh toán.";
                }
            }
            case "CONTACT_REPLY" -> {
                displayTitle = "Handmade House đã phản hồi liên hệ của bạn";
                if (displayMessage.isBlank()) {
                    displayMessage = "Nhấn để xem nội dung phản hồi.";
                }
            }
            case "REVIEW_REPLY", "REVIEW_APPROVED", "REVIEW_RESPONSE_APPROVED" -> {
                displayTitle = "Handmade House đã phản hồi đánh giá của bạn";
                if (displayMessage.isBlank()) {
                    displayMessage = "Nhấn để nhảy tới bình luận được phản hồi.";
                }
            }
            default -> {
                if (displayTitle.contains("Handmade Shop")) {
                    displayTitle = displayTitle.replace("Handmade Shop", "Handmade House");
                }
                if (displayMessage.contains("Handmade Shop")) {
                    displayMessage = displayMessage.replace("Handmade Shop", "Handmade House");
                }
            }
        }
        return new String[]{displayTitle, displayMessage};
    }

    public int countUnread(int userId) {
        ensureTable();
        if (userId <= 0) return 0;
        return getJdbi().withHandle(handle -> handle.createQuery("""
                        SELECT CAST(COUNT(*) AS SIGNED)
                        FROM notifications
                        WHERE User_Id = :userId
                          AND Is_Read = 0
                        """)
                .bind("userId", userId)
                .mapTo(Integer.class)
                .one());
    }

    public List<Map<String, Object>> getLatest(int userId, int limit) {
        ensureTable();
        if (userId <= 0) return List.of();
        int safeLimit = Math.max(1, Math.min(limit, 30));
        return getJdbi().withHandle(handle -> handle.createQuery("""
                        SELECT
                            Notification_Id AS notificationId,
                            Type AS type,
                            Title AS title,
                            Message AS message,
                            Target_Url AS targetUrl,
                            Source_Type AS sourceType,
                            Source_Id AS sourceId,
                            CAST(Is_Read AS UNSIGNED) AS isRead,
                            DATE_FORMAT(Create_At, '%d/%m/%Y %H:%i') AS createdAt
                        FROM notifications
                        WHERE User_Id = :userId
                        ORDER BY Create_At DESC, Notification_Id DESC
                        LIMIT :limit
                        """)
                .bind("userId", userId)
                .bind("limit", safeLimit)
                .mapToMap()
                .list());
    }

    public String findTargetUrl(int notificationId, int userId) {
        ensureTable();
        if (notificationId <= 0 || userId <= 0) return null;
        return getJdbi().withHandle(handle -> handle.createQuery("""
                        SELECT Target_Url
                        FROM notifications
                        WHERE Notification_Id = :notificationId
                          AND User_Id = :userId
                        LIMIT 1
                        """)
                .bind("notificationId", notificationId)
                .bind("userId", userId)
                .mapTo(String.class)
                .findOne()
                .orElse(null));
    }

    public boolean markRead(int notificationId, int userId) {
        ensureTable();
        if (notificationId <= 0 || userId <= 0) return false;
        return getJdbi().withHandle(handle -> handle.createUpdate("""
                        UPDATE notifications
                        SET Is_Read = 1,
                            Read_At = COALESCE(Read_At, NOW())
                        WHERE Notification_Id = :notificationId
                          AND User_Id = :userId
                        """)
                .bind("notificationId", notificationId)
                .bind("userId", userId)
                .execute() > 0);
    }

    public int markAllRead(int userId) {
        ensureTable();
        if (userId <= 0) return 0;
        return getJdbi().withHandle(handle -> handle.createUpdate("""
                        UPDATE notifications
                        SET Is_Read = 1,
                            Read_At = COALESCE(Read_At, NOW())
                        WHERE User_Id = :userId
                          AND Is_Read = 0
                        """)
                .bind("userId", userId)
                .execute());
    }
}
