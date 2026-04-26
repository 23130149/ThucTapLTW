package dao;

import model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDao extends BaseDao{
    public List<Category> getAllCategories() {
        String sql = "select * from categories";
        return getJdbi().withHandle(
                handle ->
                        handle.createQuery(sql)
                                .mapToBean(Category.class)
                                .list()
        );
    }
    public int getTotalProduct() {
        String sql = "select count(*) from products";
        return getJdbi().withHandle(
                handle -> handle.createQuery(sql)
                        .mapTo(int.class)
                        .one()
        );
    }
}
