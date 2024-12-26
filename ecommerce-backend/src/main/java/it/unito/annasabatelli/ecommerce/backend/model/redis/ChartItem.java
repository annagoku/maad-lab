package it.unito.annasabatelli.ecommerce.backend.model.redis;

import it.unito.annasabatelli.ecommerce.backend.model.jpa.StoreItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChartItem {
    @NotNull
    private StoreItem item;
    @Min(1)
    private int quantity;
    private double price;

    public ChartItem(int quantity, StoreItem item) {
        this.item = item;
        this.quantity = quantity;
        this.price = quantity * item.getPrice();
    }

    public void setQuantity(int q) {
        this.quantity = q;
        if(this.item != null) {
            this.price = quantity * item.getPrice();
        }
    }
    public void setItem(StoreItem i) {
        this.item = i;
        if(this.item != null) {
            this.price = quantity * item.getPrice();
        }
    }


}
