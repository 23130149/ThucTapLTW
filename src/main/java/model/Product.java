package model;

public class Product {
    private Integer productId;
    private Integer categoryId;
    private String productName;
    private String categoryName;
    private Integer productPrice;
    private Integer stockQuantity;
    private String productDescription;
    private String imageUrl;
    private Integer sold;

    public Product(Integer productId, Integer categoryId, String productName, String categoryName, Integer productPrice, Integer stockQuantity, Integer sold, String productDescription, String imageUrl) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.productPrice = productPrice;
        this.stockQuantity = stockQuantity;
        this.sold = sold;
        this.productDescription = productDescription;
        this.imageUrl = imageUrl;
    }

    public Product() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", categoryId=" + categoryId +
                ", productName='" + productName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", productPrice=" + productPrice +
                ", stockQuantity=" + stockQuantity +
                ", productDescription='" + productDescription + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", sold='" + sold + '\'' +
                '}';
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }
}