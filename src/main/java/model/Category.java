package model;

public class Category {
    private Integer categoryId;
    private String name;
    private String imageUrl;
    private int productCount;


    public Category() {
    }

    public Category(Integer categoryId, String name, String imageUrl, int productCount) {
        this.categoryId = categoryId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.productCount = productCount;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}