package cart;

import model.Product;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable {
        private Map<Integer,CartItem> data;

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
}
