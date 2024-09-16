package dto;

import entity.Product1;
import java.io.Serializable;

public class Cart_DTO implements Serializable{
    private int qty;
    private Product1 product;
    
    public Cart_DTO(){
        
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Product1 getProduct() {
        return product;
    }

    public void setProduct(Product1 product) {
        this.product = product;
    }
    
    
}
