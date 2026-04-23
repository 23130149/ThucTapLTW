package dao;

import model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDao extends BaseDao{
    public List<Category> getAllCategories() {
        String sql = "select * from category";
        return getJdbi().withHandle(
                handle ->
                        handle.createQuery(sql)
                                .mapToBean(Category.class)
                                .list()
        );
    }
}
