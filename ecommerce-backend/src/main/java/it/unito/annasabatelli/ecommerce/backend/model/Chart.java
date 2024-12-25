package it.unito.annasabatelli.ecommerce.backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter @NoArgsConstructor
public class Chart {
    private String userId;
    private List<ChartItem> items = new ArrayList<>();

    private double price = 0;
    public void addItem(int quantity, StoreItem item) {
        ChartItem ci = new ChartItem(quantity, item);
        items.add(ci);
        price += ci.getPrice();
    }
}
