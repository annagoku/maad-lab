package it.unito.annasabatelli.ecommerce.backend.model.jpa;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderId;

    @Column(nullable = false)
    private Date timeStamp;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="userId", nullable = false)
    @NotNull
    User user;

    @Column(name="paymentMethod")
    @NotNull
    String paymentMethod;

    @Column(name="price")
    @NotNull
    private double price;


    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderedItem> items;

    @Column (name="shippingAddress")
    @NotNull(message = "il campo indirizzo è obbligatorio")
    @Size(max = 100, message = "il campo indirizzo è troppo lungo (max 100 caratteri)")
    private String shippingAddress;

    @Column (name="shippingZipcode")
    @NotNull(message = "il campo CAP è obbligatorio")
    @Size(max = 6, message = "il campo CAP è troppo lungo (max 6 caratteri)")
    private String shippingZipCode;

    @Column (name="shippingProvince")
    @NotNull(message = "il campo provincia è obbligatorio")
    @Size(max = 2, message = "il campo provincia è troppo lungo (max 2 caratteri)")
    private String shippingProvince;

    @Column (name="shippingCity")
    @NotNull(message = "il campo città è obbligatorio")
    @Size(max = 50, message = "il campo città è troppo lungo (max 50 caratteri)")
    private String shippingCity;

}
