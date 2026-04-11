package cart;

import model.Product;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem  implements Serializable {
    private Product product;
    private int quantity;
    private BigDecimal price;

    public CartItem( Product product, int quantity, Integer price){
        this.product = product;
        this.quantity = Math.max(quantity, 1);
        this.price = BigDecimal.valueOf(price);
    }
    public void upQuantity( int amount){
        if(amount <= 0 ) amount = 1;
        this.quantity += amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
