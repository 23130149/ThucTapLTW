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
}
