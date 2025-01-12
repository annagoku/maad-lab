package it.unito.annasabatelli.ecommerce.backend.model.dto;

import it.unito.annasabatelli.ecommerce.backend.model.redis.ShoppingCart;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class OrderRequest {
    @NotNull
    ShoppingCart cart;
    @NotNull
    String paymentMethod;
    @NotNull
    String userMail;

    @NotNull(message = "il campo indirizzo è obbligatorio")
    @Size(max = 100, message = "il campo indirizzo è troppo lungo (max 100 caratteri)")
    private String shippingAddress;

    @NotNull(message = "il campo CAP è obbligatorio")
    @Size(max = 6, message = "il campo CAP è troppo lungo (max 6 caratteri)")
    private String shippingZipCode;

    @NotNull(message = "il campo provincia è obbligatorio")
    @Size(max = 2, message = "il campo provincia è troppo lungo (max 2 caratteri)")
    private String shippingProvince;

    @NotNull(message = "il campo città è obbligatorio")
    @Size(max = 50, message = "il campo città è troppo lungo (max 50 caratteri)")
    private String shippingCity;
}
