package dao;

import cart.Cart;
import model.Product;

import java.util.Collection;

public class CartDao extends BaseDao {
    private final ProductDao productDao = new ProductDao();

    public CartDao() {
        ensureTables();
    }

    public void ensureTables() {
        String createCarts = """
                CREATE TABLE IF NOT EXISTS carts (
                    Cart_Id INT AUTO_INCREMENT PRIMARY KEY,
                    User_Id INT NOT NULL UNIQUE,
                    Created_At DATETIME DEFAULT CURRENT_TIMESTAMP,
                    Updated_At DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
                """;

        String createCartItems = """
                CREATE TABLE IF NOT EXISTS cart_items (
                    Cart_Item_Id INT AUTO_INCREMENT PRIMARY KEY,
                    Cart_Id INT NOT NULL,
                    Product_Id INT NOT NULL,
                    Quantity INT NOT NULL DEFAULT 1,
                    Created_At DATETIME DEFAULT CURRENT_TIMESTAMP,
                    Updated_At DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    UNIQUE KEY uk_cart_product (Cart_Id, Product_Id)
                )
                """;

        getJdbi().useHandle(handle -> {
            handle.execute(createCarts);
            handle.execute(createCartItems);
        });
    }

    public int getOrCreateCartId(int userId) {
        ensureTables();

        Integer existingCartId = getJdbi().withHandle(handle ->
                handle.createQuery("SELECT Cart_Id FROM carts WHERE User_Id = :userId")
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null)
        );

        if (existingCartId != null) {
            return existingCartId;
        }

        return getJdbi().withHandle(handle ->
                handle.createUpdate("INSERT INTO carts (User_Id) VALUES (:userId)")
                        .bind("userId", userId)
                        .executeAndReturnGeneratedKeys("Cart_Id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public Cart getCartByUserId(int userId) {
        ensureTables();
        int cartId = getOrCreateCartId(userId);

        Cart cart = new Cart();

        getJdbi().useHandle(handle ->
                handle.createQuery("""
                                SELECT Product_Id AS productId, Quantity AS quantity
                                FROM cart_items
                                WHERE Cart_Id = :cartId
                                ORDER BY Cart_Item_Id DESC
                                """)
                        .bind("cartId", cartId)
                        .map((rs, ctx) -> new CartRow(
                                rs.getInt("productId"),
                                rs.getInt("quantity")
                        ))
                        .forEach(row -> {
                            Product product = productDao.getProductById(row.productId());
                            if (product != null && row.quantity() > 0) {
                                cart.addProduct(product, row.quantity());
                            }
                        })
        );

        return cart;
    }

    public void addProduct(int userId, int productId, int quantity) {
        ensureTables();
        int safeQuantity = Math.max(quantity, 1);
        int cartId = getOrCreateCartId(userId);

        String sql = """
                INSERT INTO cart_items (Cart_Id, Product_Id, Quantity)
                SELECT :cartId, p.Product_Id, LEAST(:quantity, p.Stock_Quantity)
                FROM products p
                WHERE p.Product_Id = :productId
                  AND p.Stock_Quantity > 0
                ON DUPLICATE KEY UPDATE
                    Quantity = LEAST(
                        Quantity + VALUES(Quantity),
                        (SELECT p2.Stock_Quantity FROM products p2 WHERE p2.Product_Id = :productId)
                    ),
                    Updated_At = NOW()
                """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("cartId", cartId)
                        .bind("productId", productId)
                        .bind("quantity", safeQuantity)
                        .execute()
        );
    }

    public boolean updateQuantity(int userId, int productId, int quantity) {
        ensureTables();
        int cartId = getOrCreateCartId(userId);

        if (quantity <= 0) {
            return removeProduct(userId, productId);
        }

        int updated = getJdbi().withHandle(handle ->
                handle.createUpdate("""
                                UPDATE cart_items
                                SET Quantity = LEAST(
                                        :quantity,
                                        (SELECT p.Stock_Quantity FROM products p WHERE p.Product_Id = :productId)
                                    ),
                                    Updated_At = NOW()
                                WHERE Cart_Id = :cartId AND Product_Id = :productId
                                """)
                        .bind("quantity", quantity)
                        .bind("cartId", cartId)
                        .bind("productId", productId)
                        .execute()
        );

        if (updated > 0) {
            getJdbi().useHandle(handle ->
                    handle.createUpdate("""
                                    DELETE ci
                                    FROM cart_items ci
                                    JOIN products p ON p.Product_Id = ci.Product_Id
                                    WHERE ci.Cart_Id = :cartId
                                      AND ci.Product_Id = :productId
                                      AND p.Stock_Quantity <= 0
                                    """)
                            .bind("cartId", cartId)
                            .bind("productId", productId)
                            .execute()
            );
        }

        return updated > 0;
    }

    public boolean removeProduct(int userId, int productId) {
        ensureTables();
        int cartId = getOrCreateCartId(userId);

        int deleted = getJdbi().withHandle(handle ->
                handle.createUpdate("DELETE FROM cart_items WHERE Cart_Id = :cartId AND Product_Id = :productId")
                        .bind("cartId", cartId)
                        .bind("productId", productId)
                        .execute()
        );

        return deleted > 0;
    }

    public int removeProducts(int userId, Collection<Integer> productIds) {
        ensureTables();

        if (productIds == null || productIds.isEmpty()) {
            return 0;
        }

        int deletedCount = 0;
        for (Integer productId : productIds) {
            if (productId != null && productId > 0 && removeProduct(userId, productId)) {
                deletedCount++;
            }
        }

        return deletedCount;
    }

    public void mergeSessionCartToDb(int userId, Cart sessionCart) {
        if (sessionCart == null || sessionCart.getList() == null || sessionCart.getList().isEmpty()) {
            return;
        }

        sessionCart.getList().forEach(item -> {
            if (item != null && item.getProduct() != null) {
                addProduct(userId, item.getProduct().getProductId(), item.getQuantity());
            }
        });
    }

    private record CartRow(int productId, int quantity) {
    }
}
