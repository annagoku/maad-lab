package it.unito.annasabatelli.ecommerce.backend.model.jpa;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor


@Entity
@Table(name = "stock-items")
public class StockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty
    private long stockItemId;

    @Column( name = "size")
    @JsonProperty
    private String size;

    @Column( name = "stock-quantity")
    private int stockQuantity;

    @ManyToOne(fetch = FetchType.EAGER,optional=true, cascade=CascadeType.ALL)
    @JoinColumn(name ="storeItemId")
    @JsonIgnore
    private StoreItem storeItem;








}
