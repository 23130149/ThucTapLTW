package dao;

import model.Product;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoriteDao extends BaseDao {

    public boolean isFavorite(int userId, int productId) {
        String sql = """
                SELECT COUNT(*)
                FROM favorite_products
                WHERE User_Id = :userId AND Product_Id = :productId
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bind("productId", productId)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public void addFavorite(int userId, int productId) {
        if (isFavorite(userId, productId)) {
            return;
        }

        String sql = """
                INSERT INTO favorite_products (User_Id, Product_Id, Create_At)
                VALUES (:userId, :productId, NOW())
                """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("userId", userId)
                        .bind("productId", productId)
                        .execute()
        );
    }

    public void removeFavorite(int userId, int productId) {
        String sql = """
                DELETE FROM favorite_products
                WHERE User_Id = :userId AND Product_Id = :productId
                """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("userId", userId)
                        .bind("productId", productId)
                        .execute()
        );
    }

    public boolean toggleFavorite(int userId, int productId) {
        if (isFavorite(userId, productId)) {
            removeFavorite(userId, productId);
            return false;
        }

        addFavorite(userId, productId);
        return true;
    }

    public Set<Integer> getFavoriteProductIds(int userId) {
        String sql = """
                SELECT Product_Id
                FROM favorite_products
                WHERE User_Id = :userId
                """;

        return getJdbi().withHandle(handle ->
                new HashSet<>(handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .list())
        );
    }

    public List<Product> getFavoriteProducts(int userId) {
        String sql = """
                SELECT
                    p.Product_Id AS productId,
                    p.Category_Id AS categoryId,
                    p.Product_Name AS productName,
                    c.Name AS categoryName,
                    p.Product_Price AS productPrice,
                    p.Stock_Quantity AS stockQuantity,
                    p.Product_Description AS productDescription,
                    (SELECT pi.Image_Url
                     FROM product_images pi
                     WHERE pi.Product_Id = p.Product_Id
                     ORDER BY pi.Image_Id ASC
                     LIMIT 1) AS imageUrl,
                    TRUE AS favorite
                FROM favorite_products fp
                JOIN products p ON fp.Product_Id = p.Product_Id
                JOIN categories c ON p.Category_Id = c.Category_Id
                WHERE fp.User_Id = :userId
                ORDER BY fp.Create_At DESC, fp.Favorite_Product_Id DESC
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(Product.class)
                        .list()
        );
    }


    public List<Product> getFavoriteProducts(int userId, int page, int pageSize) {
        String sql = """
                SELECT
                    p.Product_Id AS productId,
                    p.Category_Id AS categoryId,
                    p.Product_Name AS productName,
                    c.Name AS categoryName,
                    p.Product_Price AS productPrice,
                    p.Stock_Quantity AS stockQuantity,
                    p.Product_Description AS productDescription,
                    (SELECT pi.Image_Url
                     FROM product_images pi
                     WHERE pi.Product_Id = p.Product_Id
                     ORDER BY pi.Image_Id ASC
                     LIMIT 1) AS imageUrl,
                    TRUE AS favorite
                FROM favorite_products fp
                JOIN products p ON fp.Product_Id = p.Product_Id
                JOIN categories c ON p.Category_Id = c.Category_Id
                WHERE fp.User_Id = :userId
                ORDER BY fp.Create_At DESC, fp.Favorite_Product_Id DESC
                LIMIT :pageSize OFFSET :offset
                """;

        int safePage = Math.max(page, 1);
        int safePageSize = Math.max(pageSize, 1);
        int offset = (safePage - 1) * safePageSize;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bind("pageSize", safePageSize)
                        .bind("offset", offset)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public int countFavorites(int userId) {
        String sql = """
                SELECT COUNT(*)
                FROM favorite_products
                WHERE User_Id = :userId
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .one()
        );
    }
}
