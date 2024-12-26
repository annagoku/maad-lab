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
public class Chart {
    public static final String REDIS_KEY_PREFIX = "chart:";
    @NotNull
    private String userId;
    @NotNull
    private List<ChartItem> items = new ArrayList<>();

    private double price = 0;
    public void addItem(int quantity, StoreItem item) {
        ChartItem ci = new ChartItem(quantity, item);
        addItem(ci);
    }
    public void addItem(ChartItem ci) {
        items.add(ci);
        price += ci.getPrice();
    }

    public String redisKey() {
        return REDIS_KEY_PREFIX+getUserId();
    }
}
