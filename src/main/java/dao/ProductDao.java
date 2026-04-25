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

}
