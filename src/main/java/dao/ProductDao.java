package dao;

import model.Product;
import model.ProductImage;
import org.jdbi.v3.core.statement.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDao extends BaseDao{
  public List<Product> getFeaturedProducts() {
       String sql = "select p.Product_Id, p.Product_Name, p.Product_Price,  p.Category_Id, c.Name AS categoryName,(select pi.image_url from product_images pi where pi.product_id = p.product_id order by pi.image_id ASC limit 1) as imageUrl from products p join categories c on p.Category_Id = c.Category_Id order by p.Product_Id desc limit 8";
      return getJdbi().withHandle(
                handle ->
                       handle.createQuery(sql)
                                .mapToBean(Product.class)
                                .list());
   }
    public List<Product> getListProduct(){
    String sql = " select p.Product_Id, p.Product_Name, p.Product_Price,p.stock_quantity,p.product_description,  p.Category_Id, c.Name AS categoryName,(select pi.image_url from product_images pi where pi.product_id = p.product_id order by pi.image_id ASC limit 1) as imageUrl from products p join categories c on p.Category_Id = c.Category_Id";
    return getJdbi().withHandle(
            handle ->
                    handle.createQuery(sql)
                            .mapToBean(Product.class)
                            .list());
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
    public int getTotalProducts() {
        String sql = "select count(*) from products";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }
    public int getTotalStock() {
        String sql = "SELECT COALESCE(SUM(stock_quantity),0) FROM products";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(int.class)
                        .one()
        );
    }
    public int countOutOfStock() {
        String sql = "SELECT COUNT(*) FROM products WHERE stock_quantity = 0";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(int.class)
                        .one()
        );
    }
    public double getTotalValue() {
        String sql = "SELECT COALESCE(SUM(product_price * stock_quantity),0) FROM products";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(double.class)
                        .one()
        );
    }
    public Product getProductById(int id) {
        String sql = "select p.product_id AS productId, p.category_id AS categoryId, p.product_name AS productName, c.name AS categoryName, p.product_price AS productPrice, p.stock_quantity AS stockQuantity, p.product_description AS productDescription, (select pi.image_url from product_images pi where pi.product_id = p.product_id order by pi.image_id ASC limit 1) as imageUrl from products p join categories c on p.category_id = c.category_id where p.product_id = :id";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", id)
                        .mapToBean(Product.class)
                        .findOne()
                        .orElse(null)
        );
    }
    public List<Product> getRelatedProducts(int categoryId, int currentProductId, int limit) {
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
                     LIMIT 1) AS imageUrl
                FROM products p
                JOIN categories c ON p.Category_Id = c.Category_Id
                WHERE p.Category_Id = :categoryId AND p.Product_Id <> :currentProductId
                ORDER BY p.Product_Id DESC
                LIMIT :limit
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("categoryId", categoryId)
                        .bind("currentProductId", currentProductId)
                        .bind("limit", limit)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public void insertProduct(Product p) {
        String sql = "insert into products (product_name, product_price, stock_quantity, category_id, product_description) values (:name, :price, :stock, :catId, :desc)";
        int productId = getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("name", p.getProductName())
                        .bind("price", p.getProductPrice())
                        .bind("stock", p.getStockQuantity())
                        .bind("catId", p.getCategoryId())
                        .bind("desc", p.getProductDescription())
                        .executeAndReturnGeneratedKeys("product_id")
                        .mapTo(Integer.class)
                        .one()
        );
        p.setProductId(productId);
        if (p.getImageUrl() != null && !p.getImageUrl().trim().isEmpty()) {
            String imgSql = "insert into product_images (product_id, image_url) values (:productId, :imageUrl)";
            getJdbi().withHandle(handle ->
                    handle.createUpdate(imgSql)
                            .bind("productId", productId)
                            .bind("imageUrl", p.getImageUrl())
                            .execute()
            );
        }
    }
    public void updateProduct(Product p) {
        String sql = "update products set product_name = :name, product_price = :price, stock_quantity = :stock, category_id = :catId, product_description = :desc where product_id = :id";
        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", p.getProductId())
                        .bind("name", p.getProductName())
                        .bind("price", p.getProductPrice())
                        .bind("stock", p.getStockQuantity())
                        .bind("catId", p.getCategoryId())
                        .bind("desc", p.getProductDescription())
                        .execute()
        );
        if (p.getImageUrl() != null && !p.getImageUrl().trim().isEmpty()) {
            String checkSql = "select count(*) from product_images where product_id = :productId";
            int count = getJdbi().withHandle(handle ->
                    handle.createQuery(checkSql)
                            .bind("productId", p.getProductId())
                            .mapTo(Integer.class)
                            .one()
            );
            if (count > 0) {
                String imgSql = "update product_images set image_url = :imageUrl where product_id = :productId order by image_id asc limit 1";
                getJdbi().withHandle(handle ->
                        handle.createUpdate(imgSql)
                                .bind("imageUrl", p.getImageUrl())
                                .bind("productId", p.getProductId())
                                .execute()
                );
            } else {
                String imgSql = "insert into product_images (product_id, image_url) values (:productId, :imageUrl)";
                getJdbi().withHandle(handle ->
                        handle.createUpdate(imgSql)
                                .bind("productId", p.getProductId())
                                .bind("imageUrl", p.getImageUrl())
                                .execute()
                );
            }
        }
    }
    public void deleteProduct(int productId) {
        String sql = "delete from products where product_id = :id";
        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", productId)
                        .execute()
        );
    }
    public int getStockById(int productId) {
        String sql = "select stock_quantity from products where product_id = :id";

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", productId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(0)
        );
    }
    public List<Product> getFilteredProducts(String keyword, String categoryId, String status, String priceRange, int page, int pageSize) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.product_id AS productId, p.product_name AS productName, p.product_price AS productPrice, " +
                        "p.stock_quantity AS stockQuantity, p.product_description AS productDescription, " +
                        "p.category_id AS categoryId, c.name AS categoryName, " +
                        "(SELECT pi.image_url FROM product_images pi WHERE pi.product_id = p.product_id ORDER BY pi.image_id ASC LIMIT 1) AS imageUrl " +
                        "FROM products p JOIN categories c ON p.category_id = c.category_id WHERE 1=1"
        );

        Map<String, Object> params = new HashMap<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND p.product_name LIKE :keyword");
            params.put("keyword", "%" + keyword.trim() + "%");
        }

        if (categoryId != null && !categoryId.trim().isEmpty()) {
            try {
                sql.append(" AND p.category_id = :categoryId");
                params.put("categoryId", Integer.parseInt(categoryId.trim()));
            } catch (NumberFormatException ignored) {
            }
        }

        if ("instock".equals(status)) {
            sql.append(" AND p.stock_quantity > 0");
        } else if ("outofstock".equals(status)) {
            sql.append(" AND p.stock_quantity = 0");
        }

        if (priceRange != null) {
            switch (priceRange) {
                case "0-100000" -> sql.append(" AND p.product_price BETWEEN 0 AND 100000");
                case "100000-500000" -> sql.append(" AND p.product_price BETWEEN 100000 AND 500000");
                case "500000+" -> sql.append(" AND p.product_price > 500000");
            }
        }

        int safePage = Math.max(page, 1);
        int safePageSize = Math.max(pageSize, 1);
        int offset = (safePage - 1) * safePageSize;

        sql.append(" ORDER BY p.product_id ASC LIMIT :pageSize OFFSET :offset");
        params.put("pageSize", safePageSize);
        params.put("offset", offset);

        return getJdbi().withHandle(handle -> {
            Query query = handle.createQuery(sql.toString());
            params.forEach(query::bind);
            return query.mapToBean(Product.class).list();
        });
    }

    public int countFilteredProducts(String keyword, String categoryId, String status, String priceRange) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM products p WHERE 1=1"
        );

        Map<String, Object> params = new HashMap<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND p.product_name LIKE :keyword");
            params.put("keyword", "%" + keyword.trim() + "%");
        }

        if (categoryId != null && !categoryId.trim().isEmpty()) {
            try {
                sql.append(" AND p.category_id = :categoryId");
                params.put("categoryId", Integer.parseInt(categoryId.trim()));
            } catch (NumberFormatException ignored) {
            }
        }

        if ("instock".equals(status)) {
            sql.append(" AND p.stock_quantity > 0");
        } else if ("outofstock".equals(status)) {
            sql.append(" AND p.stock_quantity = 0");
        }

        if (priceRange != null) {
            switch (priceRange) {
                case "0-100000" -> sql.append(" AND p.product_price BETWEEN 0 AND 100000");
                case "100000-500000" -> sql.append(" AND p.product_price BETWEEN 100000 AND 500000");
                case "500000+" -> sql.append(" AND p.product_price > 500000");
            }
        }

        return getJdbi().withHandle(handle -> {
            Query query = handle.createQuery(sql.toString());
            params.forEach(query::bind);
            return query.mapTo(Integer.class).one();
        });
    }
    public List<ProductImage> getImagesByProductId(int productId) {
        String sql = """
        SELECT
            Image_Id AS imageId,
            Product_Id AS productId,
            Image_Url AS imageUrl
        FROM Product_Images
        WHERE Product_Id = :productId
        ORDER BY Image_Id ASC
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .mapToBean(ProductImage.class)
                        .list()
        );
    }

}
