package it.unito.annasabatelli.ecommerce.backend.model.jpa;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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

    @Column(name="status")
    private String status = "Da Pagare";

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="userId", nullable = false)
    @NotNull
    User user;


    @Column(name="price")
    @NotNull
    private double price;


    //@OneToOne
    //@JsonManagedReference
    //@JoinColumn(name = "paymentId")
    //private Payment payment;



}
