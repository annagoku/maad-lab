package it.unito.annasabatelli.ecommerce.backend.model;

import lombok.Getter;

@Getter
public class ChartItem {
    private StoreItem item;
    private int quantity;
    private double price;

    public ChartItem(int quantity, StoreItem item) {
        this.item = item;
        this.quantity = quantity;
        this.price = quantity * item.getPrice();
    }


}
