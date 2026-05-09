package dao;

import cart.CartItem;
import model.OrderItem;
import java.util.List;

public class OrderItemDao extends BaseDao {

    public List<OrderItem> getItemsByOrderId(int orderId) {

        String sql = """
            SELECT
                oi.Order_Items_Id AS orderItemId,
                oi.Order_Id       AS orderId,
                oi.Product_Id     AS productId,
                p.Product_Name    AS productName,
                oi.Unit_Price     AS unitPrice,
                oi.Quantity       AS quantity,
                (oi.Unit_Price * oi.Quantity) AS totalPrice
            FROM order_items oi
            JOIN products p
                ON oi.Product_Id = p.Product_Id
            WHERE oi.Order_Id = :orderId
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderId", orderId)
                        .mapToBean(OrderItem.class)
                        .list()
        );
    }

    public void insert(int orderId, CartItem item) {
        String sql = """
            INSERT INTO order_items (
                Order_Id,
                Product_Id,
                Quantity,
                Unit_Price
            )
            VALUES (
                :orderId,
                :productId,
                :quantity,
                :price
            )
        """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("orderId", orderId)
                        .bind("productId", item.getProduct().getProductId())
                        .bind("quantity", item.getQuantity())
                        .bind("price", item.getProduct().getProductPrice())
                        .execute()
        );
    }
}

