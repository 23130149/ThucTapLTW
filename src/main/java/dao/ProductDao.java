package dao;

import model.Product;

import java.util.List;

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
    }
    public void deleteProduct(int productId) {
        String sql = "delete from products where product_id = :id";
        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", productId)
                        .execute()
        );
    }



}
