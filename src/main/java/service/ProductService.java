package service;

import dao.ProductDao;
import model.Product;

import java.util.List;

public class ProductService {
    ProductDao pdao =new ProductDao();
    public List<Product> getListProduct() {
        return pdao.getListProduct();
    }

    public Product getProductById(int id){
        return pdao.getProductById(id);
    }
    public int getStockById(int productId){
        return pdao.getStockById(productId);
    }
}
