package it.unito.annasabatelli.ecommerce.backend.jparepo;

import it.unito.annasabatelli.ecommerce.backend.model.jpa.Order;
import it.unito.annasabatelli.ecommerce.backend.model.jpa.StockItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    //l'interfaccia fornisce tutti i metodi CRUD per la tabella

    @Query(value = "select order from Order order where order.user.email = ?1 order by order.timeStamp desc")
    List<Order> findByUserEmail(String email);


}