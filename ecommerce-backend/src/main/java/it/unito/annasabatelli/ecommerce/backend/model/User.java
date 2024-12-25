package it.unito.annasabatelli.ecommerce.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="users", uniqueConstraints = { @UniqueConstraint(columnNames = { "email"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @Column(name="password", nullable = false)
    @NotNull(message = "il campo password è obbligatorio")
    @Size(max = 10, message = "il campo passoword è troppo lungo (max 10 caratteri)")
    private String password;

    @Column(name="name", nullable = false)
    @NotNull(message = "il campo nome è obbligatorio")
    @Size(max = 50, message = "il campo nome è troppo lungo (max 50 caratteri)")
    private String name;

    @Column(name="surname", nullable = false)
    @NotNull(message = "il campo cognome è obbligatorio")
    @Size(max = 50, message = "il campo cognome è troppo lungo (max 50 caratteri)")
    private String surname;

    @Column(name="email", nullable = false)
    @NotNull(message = "il campo email  è obbligatorio")
    @Size(max = 50, message = "il campo email è troppo lungo (max 50 caratteri)")
    private String email;


    @Column (name="birthDate")
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date birthdate;


    @Column (name="address")
    @NotNull(message = "il campo indirizzo è obbligatorio")
    @Size(max = 100, message = "il campo indirizzo è troppo lungo (max 100 caratteri)")
    private String address;

    @Column (name="zipcode")
    @NotNull(message = "il campo CAP è obbligatorio")
    @Size(max = 6, message = "il campo CAP è troppo lungo (max 6 caratteri)")
    private String zipCode;

    @Column (name="province")
    @NotNull(message = "il campo provincia è obbligatorio")
    @Size(max = 2, message = "il campo provincia è troppo lungo (max 2 caratteri)")
    private String province;

    @Column (name="city")
    @NotNull(message = "il campo città è obbligatorio")
    @Size(max = 50, message = "il campo città è troppo lungo (max 50 caratteri)")
    private String city;



/*    @ManyToMany
    @JoinTable (
            name="users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "userId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "roleId"
            )
      )
*/
}
