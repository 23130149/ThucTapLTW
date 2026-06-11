package dao;

import model.Order;
import model.Product;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDao extends BaseDao {

    public List<Order> getOrdersByUserId(int userId) {

        String sql = """
        SELECT
            Order_Id        AS orderId,
            User_Id         AS userId,
            User_Address_Id AS userAddressId,
            Note            AS note,
            Status          AS status,
            Create_At       AS createAt,
            Total_Price     AS totalPrice,
            Order_Code      AS orderCode
        FROM orders
        WHERE User_Id = :userId
        ORDER BY Create_At DESC
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(Order.class)
                        .list()
        );
    }
    public List<Order> getOrdersByUserIdLimit(int userId, int limit) {
        String sql = """
        SELECT
            Order_Id        AS orderId,
            User_Id         AS userId,
            User_Address_Id AS userAddressId,
            Note            AS note,
            Status          AS status,
            Create_At       AS createAt,
            Total_Price     AS totalPrice,
            Order_Code      AS orderCode
        FROM orders
        WHERE User_Id = :userId
        ORDER BY Create_At DESC
        LIMIT :limit
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bind("limit", limit)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public List<Order> getOrdersByUserIdPaged(int userId, int limit, int offset) {
        String sql = """
        SELECT
            Order_Id        AS orderId,
            User_Id         AS userId,
            User_Address_Id AS userAddressId,
            Note            AS note,
            Status          AS status,
            Create_At       AS createAt,
            Total_Price     AS totalPrice,
            Order_Code      AS orderCode
        FROM orders
        WHERE User_Id = :userId
        ORDER BY Create_At DESC
        LIMIT :limit OFFSET :offset
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public int countOrdersByUserId(int userId) {
        String sql = """
        SELECT COUNT(*)
        FROM orders
        WHERE User_Id = :userId
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public void insert(Order order) {

        String sql = """
        INSERT INTO orders (
            User_Id,
            User_Address_Id,
            Note,
            Status,
            Create_At,
            Total_Price,
            Order_Code
        ) VALUES (
            :userId,
            :userAddressId,
            :note,
            :status,
            NOW(),
            :totalPrice,
            :orderCode
        )
    """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(order)
                        .execute()
        );
    }
    public List<Order> getRecentOrdersByUser(int userId, int limit) {

        String sql = """
        SELECT
            Order_Id            AS orderId,
            User_Id             AS userId,
            User_Address_Id     AS userAddressId,
            Note                AS note,
            Status              AS status,
            Create_At           AS createAt,
            Total_Price         AS totalPrice,
            Order_Code          AS orderCode
        FROM orders
        WHERE User_Id = :user_id
        ORDER BY Create_At DESC
        LIMIT :limit
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("user_id", userId)
                        .bind("limit", limit)
                        .mapToBean(Order.class)
                        .list()
        );
    }
    public int countOrders() {
        String sql = "select count(*) from orders";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }
    public int insertAndReturnId(Order order) {

        String sql = """
        INSERT INTO orders (
            User_Id,
            User_Address_Id,
            Payment_Method_Id,
            Ship_Address,
            ship_name,
            ship_phone,
            Note,
            Status,
            Create_At,
            Total_Price,
            Payment_Status,
            Payment_Provider,
            Order_Code
        )
        VALUES (
            :userId,
            :userAddressId,
            :paymentMethodId,
            :shipAddress,
            :shipName,
            :shipPhone,
            :note,
            :status,
            NOW(),
            :totalPrice,
            :paymentStatus,
            :paymentProvider,
            :orderCode
        )
    """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(order)
                        .executeAndReturnGeneratedKeys("Order_Id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public Order getOrderByIdAndUser(int orderId, int userId) {

        String sql = """
        SELECT
            Order_Id        AS orderId,
            User_Id         AS userId,
            User_Address_Id AS userAddressId,
            Ship_Address    AS shipAddress,
            Create_At       AS createAt,
            Total_Price     AS totalPrice,
            Status          AS status,
            Order_Code      AS orderCode,
            Note            AS note
        FROM orders
        WHERE Order_Id = :orderId
          AND User_Id = :userId
    """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .bind("orderId", orderId)
                        .bind("userId", userId)
                        .mapToBean(Order.class)
                        .findOne()
                        .orElse(null)
        );
    }


    public boolean hasUserPurchasedProduct(int userId, int productId) {

        String sql = """
        SELECT COUNT(*)
        FROM orders o
        JOIN order_items oi ON o.Order_Id = oi.Order_Id
        WHERE o.User_Id = :userId
          AND oi.Product_Id = :productId
          AND o.Status IN ('CONFIRMED','SHIPPED','COMPLETED')
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bind("productId", productId)
                        .mapTo(int.class)
                        .one() > 0
        );
    }

    public List<Order> getOrdersByUserIdAndStatusesPaged(int userId, List<String> statuses, int limit, int offset) {
        String baseSelect = """
        SELECT
            Order_Id        AS orderId,
            User_Id         AS userId,
            User_Address_Id AS userAddressId,
            Ship_Address    AS shipAddress,
            Note            AS note,
            Status          AS status,
            Create_At       AS createAt,
            Total_Price     AS totalPrice,
            Order_Code      AS orderCode
        FROM orders
        WHERE User_Id = :userId
        """;

        if (statuses == null || statuses.isEmpty()) {
            String sql = baseSelect + " ORDER BY Create_At DESC LIMIT :limit OFFSET :offset";
            return getJdbi().withHandle(handle ->
                    handle.createQuery(sql)
                            .bind("userId", userId)
                            .bind("limit", limit)
                            .bind("offset", offset)
                            .mapToBean(Order.class)
                            .list()
            );
        }

        String sql = baseSelect + " AND Status IN (<statuses>) ORDER BY Create_At DESC LIMIT :limit OFFSET :offset";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bindList("statuses", statuses)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public int countOrdersByUserIdAndStatuses(int userId, List<String> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return countOrdersByUserId(userId);
        }

        String sql = """
        SELECT COUNT(*)
        FROM orders
        WHERE User_Id = :userId
          AND Status IN (<statuses>)
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bindList("statuses", statuses)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public Map<String, Integer> countOrderStatusGroupsByUser(int userId) {
        String sql = """
        SELECT
            SUM(CASE WHEN Status IN ('PENDING', 'PROCESSING', 'CONFIRMED') THEN 1 ELSE 0 END) AS processingCount,
            SUM(CASE WHEN Status = 'SHIPPED' THEN 1 ELSE 0 END) AS shippingCount,
            SUM(CASE WHEN Status = 'COMPLETED' THEN 1 ELSE 0 END) AS completedCount,
            SUM(CASE WHEN Status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelledCount,
            SUM(CASE WHEN Status IN ('RETURN_REQUESTED', 'RETURNED', 'RETURN_REJECTED') THEN 1 ELSE 0 END) AS returnedCount,
            COUNT(*) AS allCount
        FROM orders
        WHERE User_Id = :userId
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .map((rs, ctx) -> {
                            Map<String, Integer> counts = new HashMap<>();
                            counts.put("all", rs.getInt("allCount"));
                            counts.put("processing", rs.getInt("processingCount"));
                            counts.put("shipping", rs.getInt("shippingCount"));
                            counts.put("completed", rs.getInt("completedCount"));
                            counts.put("cancelled", rs.getInt("cancelledCount"));
                            counts.put("returned", rs.getInt("returnedCount"));
                            return counts;
                        })
                        .one()
        );
    }

    public boolean cancelOrderByUser(int orderId, int userId, String reason) {
        String safeReason = reason == null ? "" : reason.trim();
        if (safeReason.isBlank()) {
            return false;
        }

        return getJdbi().inTransaction(handle -> {
            String currentStatus = handle.createQuery("""
                    SELECT Status
                    FROM orders
                    WHERE Order_Id = :orderId
                      AND User_Id = :userId
                    FOR UPDATE
                    """)
                    .bind("orderId", orderId)
                    .bind("userId", userId)
                    .mapTo(String.class)
                    .findOne()
                    .orElse(null);

            if (currentStatus == null
                    || "CANCELLED".equals(currentStatus)
                    || "COMPLETED".equals(currentStatus)
                    || "RETURN_REQUESTED".equals(currentStatus)
                    || "RETURNED".equals(currentStatus)) {
                return false;
            }

            handle.createUpdate("""
                    UPDATE products p
                    JOIN order_items oi ON p.Product_Id = oi.Product_Id
                    SET p.Stock_Quantity = p.Stock_Quantity + oi.Quantity
                    WHERE oi.Order_Id = :orderId
                    """)
                    .bind("orderId", orderId)
                    .execute();

            int updated = handle.createUpdate("""
                    UPDATE orders
                    SET Status = 'CANCELLED',
                        Note = CONCAT(
                            COALESCE(Note, ''),
                            CASE WHEN Note IS NULL OR Note = '' THEN '' ELSE '\n' END,
                            :reasonLine
                        )
                    WHERE Order_Id = :orderId
                      AND User_Id = :userId
                    """)
                    .bind("reasonLine", "Lý do hủy đơn: " + safeReason)
                    .bind("orderId", orderId)
                    .bind("userId", userId)
                    .execute();

            return updated > 0;
        });
    }

    public boolean requestReturnByUser(int orderId, int userId, String reason, String imagePath) {
        String safeReason = reason == null ? "" : reason.trim();
        String safeImagePath = imagePath == null ? "" : imagePath.trim();
        if (safeReason.isBlank()) {
            return false;
        }

        String detailLine = "Yêu cầu trả hàng: " + safeReason;
        if (!safeImagePath.isBlank()) {
            detailLine += " | Ảnh minh chứng: " + safeImagePath;
        }
        final String reasonLine = detailLine;

        String sql = """
            UPDATE orders
            SET Status = 'RETURN_REQUESTED',
                Note = CONCAT(
                    COALESCE(Note, ''),
                    CASE WHEN Note IS NULL OR Note = '' THEN '' ELSE '\n' END,
                    :reasonLine
                )
            WHERE Order_Id = :orderId
              AND User_Id = :userId
              AND Status = 'COMPLETED'
        """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("reasonLine", reasonLine)
                        .bind("orderId", orderId)
                        .bind("userId", userId)
                        .execute() > 0
        );
    }

    public List<Order> getLatestOrders(int limit) {
        String sql = """
        SELECT
            o.Order_Id AS orderId,
            o.User_Id AS userId,
            o.User_Address_Id AS userAddressId,
            o.Create_At AS createAt,
            o.Status AS status,
            o.Order_Code AS orderCode,
            o.Note AS note,
            o.Total_Price AS totalPrice,
            u.User_Name AS userName
        FROM orders o
        LEFT JOIN user u ON o.User_Id = u.User_Id
        ORDER BY o.Create_At DESC
        LIMIT :limit
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", limit)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public double getTotalRevenue() {
        String sql = """
        SELECT COALESCE(SUM(Total_Price), 0)
        FROM orders
        WHERE Status IN ('COMPLETED', 'SHIPPED')
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Double.class)
                        .one()
        );
    }

    public List<Map<String, Object>> getRevenueChart(String range) {
        int days = "30".equals(range) ? 30 : 7;

        String sql = """
        SELECT 
            DATE(Create_At) AS orderDate,
            COALESCE(SUM(Total_Price), 0) AS revenue
        FROM orders
        WHERE Create_At >= DATE_SUB(
            (
                SELECT MAX(Create_At)
                FROM orders
                WHERE Status IN ('COMPLETED', 'SHIPPED')
            ),
            INTERVAL :days DAY
        )
        AND Status IN ('COMPLETED', 'SHIPPED')
        GROUP BY DATE(Create_At)
        ORDER BY orderDate ASC
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("days", days)
                        .map((rs, ctx) -> {
                            Map<String, Object> item = new HashMap<>();
                            item.put("label", rs.getDate("orderDate").toString());
                            item.put("value", rs.getDouble("revenue"));
                            return item;
                        })
                        .list()
        );
    }

    public int countAdminNotifications() {
        String sql = """
        SELECT
            (
                SELECT COUNT(*)
                FROM orders
                WHERE Status IN ('PENDING', 'CONFIRMED')
            )
            +
            (
                SELECT COUNT(*)
                FROM reviews
                WHERE Status = 'PENDING'
            )
            +
            (
                SELECT COUNT(*)
                FROM contact
            )
            +
            (
                SELECT COUNT(*)
                FROM products
                WHERE Stock_Quantity <= 0
            ) AS total
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<Map<String, Object>> getLatestAdminNotifications(int limit) {
        String sql = """
        SELECT *
        FROM (
            SELECT 
                'ORDER' AS type,
                CONCAT('Đơn hàng ', Order_Code, ' đang chờ xử lý') AS message,
                Create_At AS createdAt,
                CONCAT('/admin/orders?detailId=', Order_Id) AS url
            FROM orders
            WHERE Status IN ('PENDING', 'CONFIRMED')

            UNION ALL

            SELECT 
                'REVIEW' AS type,
                'Có đánh giá mới đang chờ duyệt' AS message,
                Create_At AS createdAt,
                '/admin/reviews' AS url
            FROM reviews
            WHERE Status = 'PENDING'

            UNION ALL

            SELECT 
                'CONTACT' AS type,
                CONCAT('Liên hệ mới: ', Subject) AS message,
                Create_At AS createdAt,
                '/admin/contacts' AS url
            FROM contact

            UNION ALL

            SELECT 
                'STOCK' AS type,
                CONCAT('Sản phẩm ', Product_Name, ' đã hết hàng') AS message,
                NOW() AS createdAt,
                '/admin/products' AS url
            FROM products
            WHERE Stock_Quantity <= 0
        ) n
        ORDER BY createdAt DESC
        LIMIT :limit
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", limit)
                        .mapToMap()
                        .list()
        );
    }

    public double getTotalRevenueByStatus(String status) {
        String sql = """
        SELECT COALESCE(SUM(Total_Price), 0)
        FROM orders
        WHERE Status = :status
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("status", status)
                        .mapTo(Double.class)
                        .one()
        );
    }

    public List<Map<String, Object>> getRevenueChartByStatus(String range, String status) {
        int days = "30".equals(range) ? 30 : 7;

        String sql = """
        SELECT 
            DATE(Create_At) AS orderDate,
            COALESCE(SUM(Total_Price), 0) AS revenue
        FROM orders
        WHERE Status = :status
          AND Create_At >= DATE_SUB(
                (
                    SELECT MAX(Create_At)
                    FROM orders
                    WHERE Status = :status
                ),
                INTERVAL :days DAY
          )
        GROUP BY DATE(Create_At)
        ORDER BY orderDate ASC
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("days", days)
                        .bind("status", status)
                        .map((rs, ctx) -> {
                            Map<String, Object> item = new HashMap<>();
                            item.put("label", rs.getDate("orderDate").toString());
                            item.put("value", rs.getDouble("revenue"));
                            return item;
                        })
                        .list()
        );
    }
    public List<Order> getAllOrders() {
        return getAdminOrders("", "");
    }

    public List<Order> getOrdersByStatus(String status) {
        return getAdminOrders("", status);
    }

    public Order getOrderById(int orderId) {
        String sql = """
        SELECT
            o.Order_Id AS orderId,
            o.User_Id AS userId,
            o.User_Address_Id AS userAddressId,
            o.ship_address AS shipAddress,
            o.ship_name AS shipName,
            o.ship_phone AS shipPhone,
            o.Create_At AS createAt,
            o.Status AS status,
            o.Order_Code AS orderCode,
            o.Note AS note,
            o.Total_Price AS totalPrice,
            o.Payment_Status AS paymentStatus,
            u.User_Name AS userName,
            COALESCE(SUM(oi.Quantity), 0) AS totalQuantity
        FROM orders o
        LEFT JOIN user u ON o.User_Id = u.User_Id
        LEFT JOIN order_items oi ON o.Order_Id = oi.Order_Id
        WHERE o.Order_Id = :orderId
        GROUP BY
            o.Order_Id,
            o.User_Id,
            o.User_Address_Id,
            o.ship_address,
            o.ship_name,
            o.ship_phone,
            o.Create_At,
            o.Status,
            o.Order_Code,
            o.Note,
            o.Total_Price,
            o.Payment_Status,
            u.User_Name
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderId", orderId)
                        .mapToBean(Order.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public int countOrdersByStatus(String status) {
        String sql = """
        SELECT COUNT(*)
        FROM orders
        WHERE Status = :status
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("status", status)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public void updateStatus(int orderId, String status) {
        String sql = """
        UPDATE orders
        SET Status = :status
        WHERE Order_Id = :orderId
    """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("status", status)
                        .bind("orderId", orderId)
                        .execute()
        );
    }

    public boolean markVnpayPaid(String orderCode, String transactionNo, String responseCode) {
        String sql = """
        UPDATE orders
        SET
            Payment_Status = 'PAID',
            Payment_Provider = 'VNPAY',
            Payment_Transaction_No = :transactionNo,
            Payment_Response_Code = :responseCode,
            Paid_At = NOW(),
            Status = 'CONFIRMED'
        WHERE Order_Code = :orderCode
          AND Payment_Status <> 'PAID'
    """;

        int rows = getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("orderCode", orderCode)
                        .bind("transactionNo", transactionNo)
                        .bind("responseCode", responseCode)
                        .execute()
        );

        return rows > 0;
    }

    public void markVnpayFailed(String orderCode, String responseCode) {
        String sql = """
        UPDATE orders
        SET
            Payment_Status = 'FAILED',
            Payment_Provider = 'VNPAY',
            Payment_Response_Code = :responseCode,
            Status = 'PAYMENT_FAILED'
        WHERE Order_Code = :orderCode
          AND Payment_Status <> 'PAID'
    """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("orderCode", orderCode)
                        .bind("responseCode", responseCode)
                        .execute()
        );
    }

    public Order getOrderByCode(String orderCode) {
        String sql = """
        SELECT
            Order_Id AS orderId,
            User_Id AS userId,
            User_Address_Id AS userAddressId,
            Payment_Method_Id AS paymentMethodId,
            Ship_Address AS shipAddress,
            ship_name AS shipName,
            ship_phone AS shipPhone,
            Note AS note,
            Status AS status,
            Create_At AS createAt,
            Total_Price AS totalPrice,
            Payment_Status AS paymentStatus,
            Payment_Provider AS paymentProvider,
            Payment_Transaction_No AS paymentTransactionNo,
            Payment_Response_Code AS paymentResponseCode,
            Paid_At AS paidAt,
            Order_Code AS orderCode
        FROM orders
        WHERE Order_Code = :orderCode
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderCode", orderCode)
                        .mapToBean(Order.class)
                        .findOne()
                        .orElse(null)
        );
    }


    public Order getOrderByCodeAndUser(String orderCode, int userId) {
        String sql = """
        SELECT
            Order_Id AS orderId,
            User_Id AS userId,
            User_Address_Id AS userAddressId,
            Payment_Method_Id AS paymentMethodId,
            Ship_Address AS shipAddress,
            ship_name AS shipName,
            ship_phone AS shipPhone,
            Note AS note,
            Status AS status,
            Create_At AS createAt,
            Total_Price AS totalPrice,
            Payment_Status AS paymentStatus,
            Payment_Provider AS paymentProvider,
            Payment_Transaction_No AS paymentTransactionNo,
            Payment_Response_Code AS paymentResponseCode,
            Paid_At AS paidAt,
            Order_Code AS orderCode
        FROM orders
        WHERE Order_Code = :orderCode
          AND User_Id = :userId
        LIMIT 1
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderCode", orderCode)
                        .bind("userId", userId)
                        .mapToBean(Order.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public List<Order> getAdminOrders(String keyword, String status) {
        if (keyword == null) {
            keyword = "";
        }

        if (status == null) {
            status = "";
        }

        String sql = """
        SELECT
            o.Order_Id AS orderId,
            o.User_Id AS userId,
            o.User_Address_Id AS userAddressId,
            o.ship_address AS shipAddress,
            o.ship_name AS shipName,
            o.ship_phone AS shipPhone,
            o.Create_At AS createAt,
            o.Status AS status,
            o.Order_Code AS orderCode,
            o.Note AS note,
            o.Total_Price AS totalPrice,
            o.Payment_Status AS paymentStatus,
            u.User_Name AS userName,
            COALESCE(SUM(oi.Quantity), 0) AS totalQuantity
        FROM orders o
        LEFT JOIN user u ON o.User_Id = u.User_Id
        LEFT JOIN order_items oi ON o.Order_Id = oi.Order_Id
        WHERE
            (:status = '' OR o.Status = :status)
            AND (
                :keyword = ''
                OR o.Order_Code LIKE CONCAT('%', :keyword, '%')
                OR CAST(o.Order_Id AS CHAR) LIKE CONCAT('%', :keyword, '%')
                OR u.User_Name LIKE CONCAT('%', :keyword, '%')
                OR o.ship_name LIKE CONCAT('%', :keyword, '%')
                OR o.ship_phone LIKE CONCAT('%', :keyword, '%')
            )
        GROUP BY
            o.Order_Id,
            o.User_Id,
            o.User_Address_Id,
            o.ship_address,
            o.ship_name,
            o.ship_phone,
            o.Create_At,
            o.Status,
            o.Order_Code,
            o.Note,
            o.Total_Price,
            o.Payment_Status,
            u.User_Name
        ORDER BY o.Create_At DESC
    """;

        String finalKeyword = keyword;
        String finalStatus = status;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("keyword", finalKeyword)
                        .bind("status", finalStatus)
                        .mapToBean(Order.class)
                        .list()
        );
    }
    public Order getOrderByCodeAndUser(String orderCode, int userId) {
        String sql = """
        SELECT
            Order_Id AS orderId,
            User_Id AS userId,
            User_Address_Id AS userAddressId,
            Payment_Method_Id AS paymentMethodId,
            Note AS note,
            Status AS status,
            Create_At AS createAt,
            Total_Price AS totalPrice,
            Payment_Status AS paymentStatus,
            Payment_Provider AS paymentProvider,
            Payment_Transaction_No AS paymentTransactionNo,
            Payment_Response_Code AS paymentResponseCode,
            Paid_At AS paidAt,
            Order_Code AS orderCode,
            ship_name AS shipName,
            ship_phone AS shipPhone,
            ship_address AS shipAddress
        FROM orders
        WHERE Order_Code = :orderCode
          AND User_Id = :userId
        LIMIT 1
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderCode", orderCode)
                        .bind("userId", userId)
                        .mapToBean(Order.class)
                        .findOne()
                        .orElse(null)
        );
    }
}