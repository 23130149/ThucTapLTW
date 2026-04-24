package dao;

import model.Order;
import model.Product;

import java.util.LinkedHashMap;
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
      AND Status <> 'PROCESSING'
    ORDER BY Create_At DESC
""";

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(Order.class)
                        .list()
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
    public List<Order> getLatestOrders(int limit) {
        String sql = "select  o.Order_Id as orderId,o.User_Id as userId, o.User_Address_Id as userAddressId,  o.Create_At as createAt, o.Status as status, o.Order_Code as orderCode, o.Note as note, o.Total_Price as totalPrice, u.user_name as userName, p.Product_Name  as productName, oi.Quantity as quantity from orders o  join user u ON o.user_id = u.user_id join order_items oi ON o.order_id = oi.order_id join products p ON oi.product_id = p.product_id order by o.Create_At desc limit :limit";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", limit)
                        .mapToBean(Order.class)
                        .list()
        );
    }
    public List<Product> getTopProducts(int limit) {
        String sql = """
            SELECT 
                p.product_id AS productId,
                p.product_name AS productName,
                p.product_price AS productPrice,
                SUM(oi.quantity) AS sold,
                SUM(oi.quantity * p.product_price) AS revenue
            FROM products p
            JOIN order_items oi ON p.product_id = oi.product_id
            JOIN orders o ON oi.order_id = o.order_id
            WHERE o.Status IN ('COMPLETED', 'SHIPPED')
            GROUP BY p.product_id, p.product_name, p.product_price
            ORDER BY sold DESC
            LIMIT :limit
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", limit)
                        .mapToBean(Product.class)
                        .list()
        );
    }
    public double getTotalRevenue() {
        String sql = """
            SELECT COALESCE(SUM(o.Total_Price), 0)
            FROM orders o
            WHERE YEAR(o.Create_At) = YEAR(CURDATE())
              AND MONTH(o.Create_At) = MONTH(CURDATE())
              AND o.Status IN ('COMPLETED', 'SHIPPED')
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Double.class)
                        .one()
        );
    }
    public int insertAndReturnId(Order order) {

        String sql = """
        INSERT INTO orders (
            User_Id,
            User_Address_Id,
            Ship_Address,
            Note,
            Status,
            Create_At,
            Total_Price,
            Order_Code
        )
        VALUES (
            :userId,
            :userAddressId,
            :shipAddress,
            :note,
            :status,
            NOW(),
            :totalPrice,
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
        FROM Orders o
        JOIN Order_Items oi ON o.Order_Id = oi.Order_Id
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
    public void updateStatus(int orderId, String status) {
        String sql = "UPDATE orders SET Status = :status WHERE Order_Id = :id";

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("status", status)
                        .bind("id", orderId)
                        .execute()
        );}


    public List<Order> getAllOrders() {
        String sql = """
        SELECT
            o.Order_Id            AS orderId,
            o.Order_Code          AS orderCode,
            u.User_Name           AS userName,
            COUNT(oi.Product_Id)  AS totalQuantity,
            o.Total_Price         AS totalPrice,
            o.Create_At           AS createAt,
            o.Status              AS status
        FROM orders o
        JOIN user u ON o.User_Id = u.User_Id
        JOIN order_items oi ON o.Order_Id = oi.Order_Id
        GROUP BY
            o.Order_Id, o.Order_Code, u.User_Name,
            o.Total_Price, o.Create_At, o.Status
        ORDER BY o.Create_At DESC
    """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public List<Order> getOrdersByStatus(String status) {
        String sql = """
        SELECT
            o.Order_Id            AS orderId,
            o.Order_Code          AS orderCode,
            u.User_Name           AS userName,
            COUNT(oi.Product_Id)  AS totalQuantity,
            o.Total_Price         AS totalPrice,
            o.Create_At           AS createAt,
            o.Status              AS status
        FROM orders o
        JOIN user u ON o.User_Id = u.User_Id
        JOIN order_items oi ON o.Order_Id = oi.Order_Id
        WHERE o.Status = :status
        GROUP BY
            o.Order_Id, o.Order_Code, u.User_Name,
            o.Total_Price, o.Create_At, o.Status
        ORDER BY o.Create_At DESC
    """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .bind("status", status)
                        .mapToBean(Order.class)
                        .list()
        );
    }
    public int countOrdersByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM orders WHERE Status = :status";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("status", status)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public Map<String, Double> getRevenueChart(String range) {
        Map<String, Double> chart = new LinkedHashMap<>();
        int days = "30".equals(range) ? 30 : 7;

        String sql = """
            SELECT DATE(o.Create_At) AS order_date,
                   COALESCE(SUM(oi.quantity * p.product_price), 0) AS daily_revenue
            FROM orders o
            JOIN order_items oi ON o.Order_Id = oi.Order_Id
            JOIN products p ON oi.product_id = p.product_id
            WHERE o.Create_At >= DATE_SUB(CURDATE(), INTERVAL :days DAY)
              AND o.Status IN ('COMPLETED', 'SHIPPED')
            GROUP BY DATE(o.Create_At)
            ORDER BY order_date ASC
        """;

        getJdbi().withHandle(handle -> {
            handle.createQuery(sql)
                    .bind("days", days)
                    .map((rs, ctx) -> {
                        java.sql.Date sqlDate = rs.getDate("order_date");
                        if (sqlDate != null) {
                            String dateStr = sqlDate.toLocalDate()
                                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM"));
                            double rev = rs.getDouble("daily_revenue");
                            chart.put(dateStr, rev);
                        }
                        return null;
                    })
                    .list();
            return null;
        });

        return chart;
    }
}