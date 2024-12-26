package it.unito.annasabatelli.ecommerce.backend.jparepo;

import it.unito.annasabatelli.ecommerce.backend.model.jpa.StoreItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StoreRepository extends CrudRepository<StoreItem, Long> {
    //l'interfaccia fornisce tutti i metodi CRUD per la tabella


    List<StoreItem>  findAll();




}