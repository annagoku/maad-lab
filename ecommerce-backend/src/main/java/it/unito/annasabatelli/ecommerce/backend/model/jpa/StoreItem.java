package it.unito.annasabatelli.ecommerce.backend.model.jpa;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "store-items")
public class StoreItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long storeItemId;

    @Column( name = "name")
    private String name;

    @Column( name = "description")
    private String description;

    @Column( name = "size")
    private String size;

    @Column( name = "price")
    private double price;

    @Column( name = "stock-quantity")
    private int stockQuantity;


    @Column( name = "picture-base64")
    private String pictureBase64;



//    @OneToMany(mappedBy = "storeItemType")
//    @JsonIgnore
//    private List<StoreItem> storeItems;

}

