package dao;

import model.Category;

import java.util.List;

public class CategoryDao extends BaseDao {
    public List<Category> getAllCategories() {
        String sql = """
                SELECT
                    c.category_id AS categoryId,
                    c.name AS name,
                    c.image_url AS imageUrl,
                    COUNT(p.product_id) AS productCount
                FROM categories c
                LEFT JOIN products p ON p.category_id = c.category_id
                GROUP BY c.category_id, c.name, c.image_url
                ORDER BY c.name ASC
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Category.class)
                        .list()
        );
    }

    public int getTotalProduct() {
        String sql = "select count(*) from products";
        return getJdbi().withHandle(handle -> handle.createQuery(sql)
                .mapTo(int.class)
                .one()
        );
    }
    public List<Category> searchCategories(String keyword) {
        String sql = """
                SELECT
                    c.category_id AS categoryId,
                    c.name AS name,
                    c.image_url AS imageUrl,
                    COUNT(p.product_id) AS productCount
                FROM categories c
                LEFT JOIN products p ON p.category_id = c.category_id
                WHERE c.name LIKE :keyword
                GROUP BY c.category_id, c.name, c.image_url
                ORDER BY c.name ASC
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("keyword", "%" + keyword + "%")
                        .mapToBean(Category.class)
                        .list()
        );
    }
    public void addCategory(String name, String imageUrl) {
        String sql = """
                INSERT INTO categories(name, image_url)
                VALUES (:name, :imageUrl)
                """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("name", name)
                        .bind("imageUrl", imageUrl)
                        .execute()
        );
    }

    public boolean updateCategory(int categoryId, String name, String imageUrl) {
        String sql = """
                UPDATE categories
                SET name = :name,
                    image_url = :imageUrl
                WHERE category_id = :categoryId
                """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("name", name)
                        .bind("imageUrl", imageUrl)
                        .bind("categoryId", categoryId)
                        .execute() > 0
        );
    }

    public boolean deleteCategory(int categoryId) {
        String sql = """
                DELETE FROM categories
                WHERE category_id = :categoryId
                """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("categoryId", categoryId)
                        .execute() > 0
        );
    }

    public boolean hasProducts(int categoryId) {
        String sql = "SELECT COUNT(*) FROM products WHERE category_id = :categoryId";

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("categoryId", categoryId)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }
}

