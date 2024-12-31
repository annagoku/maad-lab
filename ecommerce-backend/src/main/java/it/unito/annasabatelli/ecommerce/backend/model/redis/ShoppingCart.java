package it.unito.annasabatelli.ecommerce.backend.model.redis;

import it.unito.annasabatelli.ecommerce.backend.model.jpa.StoreItem;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter @NoArgsConstructor
public class ShoppingCart {
    public static final String REDIS_KEY_PREFIX = "cart:";
    @NotNull
    private String userId;
    @NotNull
    private List<ShoppingCartItem> items = new ArrayList<>();

    private double price = 0;
    public void addItem(int quantity, StoreItem item) {
        ShoppingCartItem ci = new ShoppingCartItem(quantity, item);
        addItem(ci);
    }
    public void addItem(ShoppingCartItem ci) {
        items.add(ci);
        price += ci.getQuantity()*ci.getSize().getStoreItem().getPrice();
    }

    public String redisKey() {
        return REDIS_KEY_PREFIX+getUserId();
    }
}
