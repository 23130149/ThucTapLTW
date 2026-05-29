package dao;

import cart.CartItem;
import model.OrderItem;
import java.util.List;

public class OrderItemDao extends BaseDao {

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
    public List<OrderItem> getItemsByOrderId(int orderId) {
        String sql = """
            SELECT
                0 AS orderItemId,
                oi.Order_Id AS orderId,
                oi.Product_Id AS productId,
                oi.Quantity AS quantity,
                oi.Unit_Price AS unitPrice,
                (oi.Quantity * oi.Unit_Price) AS totalPrice,
                p.Product_Name AS productName,
                (
                    SELECT pi.Image_Url
                    FROM product_images pi
                    WHERE pi.Product_Id = p.Product_Id
                    ORDER BY pi.Image_Id ASC
                    LIMIT 1
                ) AS imageUrl
            FROM order_items oi
            JOIN products p ON oi.Product_Id = p.Product_Id
            WHERE oi.Order_Id = :orderId
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderId", orderId)
                        .mapToBean(OrderItem.class)
                        .list()
        );
    }
}


