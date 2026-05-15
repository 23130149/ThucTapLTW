package cart;

import model.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable {
        private Map<Integer,CartItem> data;
    public Map<Integer, CartItem> getData() {
        return data;
    }
        public Cart(){data = new HashMap<Integer, CartItem>();}
    public void addProduct(Product p, int quantity ){
        if( quantity <= 0) quantity = 1;
        CartItem item =data.get(p.getProductId());
        if(item!= null){
            item.upQuantity(quantity);
        }else{
            data.put(p.getProductId(), new CartItem(p,quantity,p.getProductPrice()));
        }
    }
    public boolean update(int ProductId, int quantity){
        CartItem item = data.get(ProductId);
        if(item == null) return false;
        item.setQuantity(quantity);
        return true;
    }
    public CartItem deleteProduct(int ProductId){
        return data.remove(ProductId);
    }

    public int getTotalQuantity(){
        int total = 0;
        for (CartItem item : data.values()){
            total += item.getQuantity();
        }
        return total;
    }

    public double getTotalPrice(){
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : data.values()){
            total =total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total.doubleValue();
    }
}
