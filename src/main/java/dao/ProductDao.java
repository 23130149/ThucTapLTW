package dao;

import model.Product;

import java.util.List;

public class ProductDao extends BaseDao{
  public List<Product> getFeaturedProducts() {
       String sql = "select p.productId, p.productName, p.productPrice, p.imageUrl, p.categoryId, c.name AS categoryName from products p join categories c on p.categoryId = c.categoryId order by p.sold desc limit 8";
      return getJdbi().withHandle(
                handle ->
                       handle.createQuery(sql)
                                .mapToBean(Product.class)
                                .list());
   }
}
