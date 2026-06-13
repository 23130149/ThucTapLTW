package dao;

import model.Product;
import model.ProductImage;
import org.jdbi.v3.core.statement.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDao extends BaseDao {
    private static final String PRODUCT_SELECT = """
            SELECT
                p.product_id AS productId,
                p.category_id AS categoryId,
                p.product_name AS productName,
                c.name AS categoryName,
                p.product_price AS productPrice,
                p.stock_quantity AS stockQuantity,
                p.product_description AS productDescription,
                COALESCE(s.sold, 0) AS sold,
                (SELECT pi.image_url
                 FROM product_images pi
                 WHERE pi.product_id = p.product_id
                 ORDER BY pi.image_id ASC
                 LIMIT 1) AS imageUrl
            FROM products p
            JOIN categories c ON p.category_id = c.category_id
            LEFT JOIN (
                SELECT oi.product_id, SUM(oi.quantity) AS sold
                FROM order_items oi
                JOIN orders o ON oi.order_id = o.order_id
                WHERE o.status IN ('CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'COMPLETED')
                GROUP BY oi.product_id
            ) s ON s.product_id = p.product_id
            WHERE 1=1
            """;

    public List<Product> getFeaturedProducts() {
        String sql = "select p.Product_Id, p.Product_Name, p.Product_Price,  p.Category_Id, c.Name AS categoryName,(select pi.image_url from product_images pi where pi.product_id = p.product_id order by pi.image_id ASC limit 1) as imageUrl from products p join categories c on p.Category_Id = c.Category_Id order by p.Product_Id desc limit 8";
        return getJdbi().withHandle(
                handle ->
                        handle.createQuery(sql)
                                .mapToBean(Product.class)
                                .list());
    }

    public List<Product> getListProduct() {
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
            p.Product_Id AS productId,
            p.Product_Name AS productName,
            p.Product_Price AS productPrice,
            p.Stock_Quantity AS stockQuantity,
            COALESCE(SUM(oi.Quantity), 0) AS sold,
            COALESCE(SUM(oi.Quantity * oi.Unit_Price), 0) AS revenue,
            (
                SELECT pi.Image_Url
                FROM product_images pi
                WHERE pi.Product_Id = p.Product_Id
                ORDER BY pi.Image_Id ASC
                LIMIT 1
            ) AS imageUrl
        FROM products p
        JOIN order_items oi ON p.Product_Id = oi.Product_Id
        JOIN orders o ON oi.Order_Id = o.Order_Id
        WHERE o.Status IN ('COMPLETED', 'DELIVERED', 'SHIPPED')
        GROUP BY 
            p.Product_Id,
            p.Product_Name,
            p.Product_Price,
            p.Stock_Quantity
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
    public List<Product> getTopProductsByStatus(int limit, String status) {
        String sql = """
        SELECT
            p.Product_Id AS productId,
            p.Product_Name AS productName,
            p.Product_Price AS productPrice,
            p.Stock_Quantity AS stockQuantity,
            COALESCE(SUM(oi.Quantity), 0) AS sold,
            COALESCE(SUM(oi.Quantity * oi.Unit_Price), 0) AS revenue,
            (
                SELECT pi.Image_Url
                FROM product_images pi
                WHERE pi.Product_Id = p.Product_Id
                ORDER BY pi.Image_Id ASC
                LIMIT 1
            ) AS imageUrl
        FROM products p
        JOIN order_items oi ON p.Product_Id = oi.Product_Id
        JOIN orders o ON oi.Order_Id = o.Order_Id
        WHERE o.Status = :status
        GROUP BY
            p.Product_Id,
            p.Product_Name,
            p.Product_Price,
            p.Stock_Quantity
        ORDER BY sold DESC
        LIMIT :limit
    """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("status", status)
                        .bind("limit", limit)
                        .mapToBean(Product.class)
                        .list()
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

    public boolean deleteProduct(int productId) {
        String sql = "delete from products where product_id = :id";
        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", productId)
                        .execute() > 0
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
        return getFilteredProducts(keyword, categoryId, status, priceRange, null, null, page, pageSize);
    }

    public List<Product> getFilteredProducts(String keyword, String categoryId, String status, String priceRange,
                                             String material, String usage, int page, int pageSize) {
        List<Integer> categoryIds = new ArrayList<>();
        String cleaned = clean(categoryId);
        if (cleaned != null) {
            try {
                categoryIds.add(Integer.parseInt(cleaned));
            } catch (NumberFormatException ignored) {
            }
        }
        return getFilteredProducts(keyword, categoryIds, status, priceRange, material, usage, null, page, pageSize);
    }

    public List<Product> getFilteredProducts(String keyword, List<Integer> categoryIds, String status, String priceRange,
                                             String sort, int page, int pageSize) {
        return getFilteredProducts(keyword, categoryIds, status, priceRange, null, null, sort, page, pageSize);
    }

    public List<Product> getFilteredProducts(String keyword, List<Integer> categoryIds, String status, String priceRange,
                                             String material, String usage, String sort, int page, int pageSize) {
        StringBuilder sql = new StringBuilder(PRODUCT_SELECT);
        Map<String, Object> params = new HashMap<>();

        appendFilters(sql, params, keyword, categoryIds, status, priceRange, material, usage);
        appendSort(sql, sort);

        int safePage = Math.max(page, 1);
        int safePageSize = Math.max(pageSize, 1);
        int offset = (safePage - 1) * safePageSize;

        sql.append(" LIMIT :pageSize OFFSET :offset");
        params.put("pageSize", safePageSize);
        params.put("offset", offset);

        return getJdbi().withHandle(handle -> {
            Query query = handle.createQuery(sql.toString());
            params.forEach(query::bind);
            bindCategoryIds(query, categoryIds);
            return query.mapToBean(Product.class).list();
        });
    }

    public int countFilteredProducts(String keyword, String categoryId, String status, String priceRange) {
        return countFilteredProducts(keyword, categoryId, status, priceRange, null, null);
    }

    public int countFilteredProducts(String keyword, String categoryId, String status, String priceRange,
                                     String material, String usage) {
        List<Integer> categoryIds = new ArrayList<>();
        String cleaned = clean(categoryId);
        if (cleaned != null) {
            try {
                categoryIds.add(Integer.parseInt(cleaned));
            } catch (NumberFormatException ignored) {
            }
        }
        return countFilteredProducts(keyword, categoryIds, status, priceRange, material, usage);
    }

    public int countFilteredProducts(String keyword, List<Integer> categoryIds, String status, String priceRange) {
        return countFilteredProducts(keyword, categoryIds, status, priceRange, null, null);
    }

    public int countFilteredProducts(String keyword, List<Integer> categoryIds, String status, String priceRange,
                                     String material, String usage) {
        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*)
                FROM products p
                JOIN categories c ON p.category_id = c.category_id
                WHERE 1=1
                """);

        Map<String, Object> params = new HashMap<>();
        appendFilters(sql, params, keyword, categoryIds, status, priceRange, material, usage);

        return getJdbi().withHandle(handle -> {
            Query query = handle.createQuery(sql.toString());
            params.forEach(query::bind);
            bindCategoryIds(query, categoryIds);
            return query.mapTo(Integer.class).one();
        });
    }

    public List<Product> searchSuggestions(String keyword, int limit) {
        String cleanedKeyword = clean(keyword);

        StringBuilder sql = new StringBuilder(PRODUCT_SELECT);
        Map<String, Object> params = new HashMap<>();
        if (cleanedKeyword != null) {
            appendKeywordFilter(sql, params, cleanedKeyword);
            appendSuggestionSort(sql, params, cleanedKeyword);
        } else {
            sql.append(" ORDER BY COALESCE(s.sold, 0) DESC, p.product_id DESC");
        }
        sql.append(" LIMIT :limit");
        params.put("limit", Math.max(1, Math.min(limit, 4)));

        return getJdbi().withHandle(handle -> {
            Query query = handle.createQuery(sql.toString());
            params.forEach(query::bind);
            return query.mapToBean(Product.class).list();
        });
    }

    private void appendSuggestionSort(StringBuilder sql, Map<String, Object> params, String keyword) {
        List<String> terms = flattenKeywordTerms(keyword);
        if (terms.isEmpty()) {
            sql.append(" ORDER BY COALESCE(s.sold, 0) DESC, p.product_id DESC");
            return;
        }

        sql.append(" ORDER BY (0");
        for (int i = 0; i < terms.size(); i++) {
            String exact = "rankExact" + i;
            String prefix = "rankPrefix" + i;
            String contains = "rankContains" + i;

            sql.append(" + CASE WHEN LOWER(p.product_name) = LOWER(:").append(exact).append(") THEN 120 ELSE 0 END")
                    .append(" + CASE WHEN LOWER(p.product_name) LIKE LOWER(:").append(prefix).append(") THEN 80 ELSE 0 END")
                    .append(" + CASE WHEN LOWER(p.product_name) LIKE LOWER(:").append(contains).append(") THEN 55 ELSE 0 END")
                    .append(" + CASE WHEN LOWER(c.name) LIKE LOWER(:").append(contains).append(") THEN 35 ELSE 0 END")
                    .append(" + CASE WHEN LOWER(p.product_description) LIKE LOWER(:").append(contains).append(") THEN 15 ELSE 0 END");

            params.put(exact, terms.get(i));
            params.put(prefix, terms.get(i) + "%");
            params.put(contains, "%" + terms.get(i) + "%");
        }
        sql.append(") DESC, COALESCE(s.sold, 0) DESC, p.product_id DESC");
    }

    private void appendFilters(StringBuilder sql, Map<String, Object> params, String keyword, List<Integer> categoryIds,
                               String status, String priceRange, String material, String usage) {
        appendKeywordFilter(sql, params, keyword);
        appendCategoryFilter(sql, categoryIds);
        appendStatusFilter(sql, status);
        appendPriceFilter(sql, priceRange);
        appendMaterialFilter(sql, params, material);
        appendUsageFilter(sql, params, usage);
    }

    private void appendKeywordFilter(StringBuilder sql, Map<String, Object> params, String keyword) {
        String cleaned = clean(keyword);
        if (cleaned == null) return;

        List<List<String>> groups = splitKeywordGroups(cleaned);
        for (int groupIndex = 0; groupIndex < groups.size(); groupIndex++) {
            List<String> terms = groups.get(groupIndex);
            if (terms.isEmpty()) continue;

            sql.append(" AND (");
            for (int termIndex = 0; termIndex < terms.size(); termIndex++) {
                if (termIndex > 0) sql.append(" OR ");
                String paramName = "keyword" + groupIndex + "_" + termIndex;
                appendKeywordMatch(sql, paramName);
                params.put(paramName, "%" + terms.get(termIndex) + "%");
            }
            sql.append(")");
        }
    }

    private void appendKeywordMatch(StringBuilder sql, String paramName) {
        sql.append("(p.product_name LIKE :").append(paramName)
                .append(" OR p.product_description LIKE :").append(paramName)
                .append(" OR c.name LIKE :").append(paramName)
                .append(")");
    }

    private List<List<String>> splitKeywordGroups(String keyword) {
        List<List<String>> groups = new ArrayList<>();
        if (keyword.contains("+")) {
            for (String part : keyword.split("\\+")) {
                List<String> group = splitWords(part);
                if (!group.isEmpty()) groups.add(group);
            }
        } else {
            List<String> words = splitWords(keyword);
            if (!words.isEmpty()) groups.add(words);
        }
        return groups;
    }

    private List<String> flattenKeywordTerms(String keyword) {
        List<String> terms = new ArrayList<>();
        for (List<String> group : splitKeywordGroups(keyword)) {
            terms.addAll(group);
        }
        return terms;
    }

    private List<String> splitWords(String value) {
        String cleaned = clean(value);
        if (cleaned == null) return List.of();

        List<String> words = new ArrayList<>();
        for (String word : cleaned.split("\\s+")) {
            String safeWord = clean(word);
            if (safeWord != null) {
                words.add(safeWord);
            }
        }
        return words;
    }

    private void appendCategoryFilter(StringBuilder sql, List<Integer> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) return;
        sql.append(" AND p.category_id IN (<categoryIds>)");
    }

    private void bindCategoryIds(Query query, List<Integer> categoryIds) {
        if (categoryIds != null && !categoryIds.isEmpty()) {
            query.bindList("categoryIds", categoryIds);
        }
    }

    private void appendStatusFilter(StringBuilder sql, String status) {
        if ("instock".equals(status)) {
            sql.append(" AND p.stock_quantity > 0");
        } else if ("lowstock".equals(status)) {
            sql.append(" AND p.stock_quantity > 0 AND p.stock_quantity < 5");
        } else if ("outofstock".equals(status)) {
            sql.append(" AND p.stock_quantity = 0");
        }
    }

    private void appendPriceFilter(StringBuilder sql, String priceRange) {
        if (priceRange == null) return;

        switch (priceRange) {
            case "0-100000" -> sql.append(" AND p.product_price BETWEEN 0 AND 100000");
            case "100000-300000" -> sql.append(" AND p.product_price BETWEEN 100000 AND 300000");
            case "300000-500000" -> sql.append(" AND p.product_price BETWEEN 300000 AND 500000");
            case "100000-500000" -> sql.append(" AND p.product_price BETWEEN 100000 AND 500000");
            case "500000+" -> sql.append(" AND p.product_price > 500000");
        }
    }

    private void appendMaterialFilter(StringBuilder sql, Map<String, Object> params, String material) {
        String cleaned = clean(material);
        if (cleaned == null || "all".equals(cleaned)) return;

        String label = switch (cleaned) {
            case "len" -> "len";
            case "vai" -> "vải";
            case "go" -> "gỗ";
            case "giay" -> "giấy";
            case "da" -> "da";
            case "hat" -> "hạt";
            case "soi" -> "sợi";
            default -> cleaned;
        };

        sql.append(" AND (p.product_name LIKE :material OR p.product_description LIKE :material OR c.name LIKE :material)");
        params.put("material", "%" + label + "%");
    }

    private void appendUsageFilter(StringBuilder sql, Map<String, Object> params, String usage) {
        String cleaned = clean(usage);
        if (cleaned == null || "all".equals(cleaned)) return;

        List<String> words = switch (cleaned) {
            case "trang-tri" -> List.of("trang trí", "decor", "nhà", "phòng");
            case "thoi-trang" -> List.of("thời trang", "phụ kiện", "túi", "vòng", "áo");
            case "qua-tang" -> List.of("quà", "tặng", "sinh nhật", "kỷ niệm");
            case "gia-dung" -> List.of("gia dụng", "bếp", "nhà", "khay", "ly");
            default -> List.of(cleaned);
        };

        sql.append(" AND (");
        for (int i = 0; i < words.size(); i++) {
            if (i > 0) sql.append(" OR ");
            String paramName = "usage" + i;
            sql.append("p.product_name LIKE :").append(paramName)
                    .append(" OR p.product_description LIKE :").append(paramName)
                    .append(" OR c.name LIKE :").append(paramName);
            params.put(paramName, "%" + words.get(i) + "%");
        }
        sql.append(")");
    }

    private void appendSort(StringBuilder sql, String sort) {
        switch (sort == null ? "" : sort) {
            case "price-asc" -> sql.append(" ORDER BY p.product_price ASC, p.product_id DESC");
            case "price-desc" -> sql.append(" ORDER BY p.product_price DESC, p.product_id DESC");
            case "best-selling" -> sql.append(" ORDER BY COALESCE(s.sold, 0) DESC, p.product_id DESC");
            case "newest" -> sql.append(" ORDER BY p.product_id DESC");
            default -> sql.append(" ORDER BY p.product_id DESC");
        }
    }

    private String clean(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
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
