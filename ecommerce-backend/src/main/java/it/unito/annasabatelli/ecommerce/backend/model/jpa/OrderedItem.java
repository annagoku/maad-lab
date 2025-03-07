package it.unito.annasabatelli.ecommerce.backend.model.jpa;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "order-items")
public class OrderedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderedItemId;

    @Column( name = "price")
    private double price;

    @Column( name = "quantity")
    private int quantity;

    @Column( name = "size")
    private String size;
    //@JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER,optional=true, cascade=CascadeType.MERGE)
    @JoinColumn(name ="storeItemId")
    private StoreItem storeItem;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY,optional=true, cascade=CascadeType.ALL)
    @JoinColumn(name="orderId")
    private Order order;

}
