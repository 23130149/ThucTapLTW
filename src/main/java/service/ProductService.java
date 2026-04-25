package service;

import dao.ProductDao;
import model.Product;

import java.util.List;

public class ProductService {
    ProductDao pdao =new ProductDao();
    public List<Product> getListProduct() {
        return pdao.getListProduct();}
}
