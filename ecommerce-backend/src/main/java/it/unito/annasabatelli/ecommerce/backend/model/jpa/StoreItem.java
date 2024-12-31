package it.unito.annasabatelli.ecommerce.backend.model.jpa;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE
)
@Entity
@Table(name = "store-items")
public class StoreItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty
    private long storeItemId;

    @Column( name = "name")
    @JsonProperty
    private String name;

    @Column( name = "description")
    @JsonProperty
    private String description;

    @Column( name = "price")
    @JsonProperty
    private double price;

    @Column( name = "picture-path")
    @JsonProperty
    private String picturePath;



   @OneToMany(mappedBy = "storeItem")
   @JsonIgnore
   private List<StockItem> stockItems;

}

