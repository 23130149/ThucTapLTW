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
}
