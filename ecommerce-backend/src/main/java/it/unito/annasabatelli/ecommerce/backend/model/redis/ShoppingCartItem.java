package it.unito.annasabatelli.ecommerce.backend.model.redis;

import it.unito.annasabatelli.ecommerce.backend.model.jpa.StockItem;
import it.unito.annasabatelli.ecommerce.backend.model.jpa.StoreItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShoppingCartItem {
    @NotNull
    private StoreItem storeItem;
    @Min(1)
    private int quantity;

    @NotNull
    private StockItem size;

    public ShoppingCartItem(int quantity, StoreItem item) {
        this.storeItem = item;
        this.quantity = quantity;

    }

}
