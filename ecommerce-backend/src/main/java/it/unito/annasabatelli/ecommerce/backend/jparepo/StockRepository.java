package it.unito.annasabatelli.ecommerce.backend.jparepo;

import it.unito.annasabatelli.ecommerce.backend.model.jpa.StockItem;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockRepository extends CrudRepository<StockItem, Long> {
    //l'interfaccia fornisce tutti i metodi CRUD per la tabella


    // Taglie
    @Query(value = "select si from StockItem si where si.storeItem.storeItemId = ?1 order by si.size")
    List<StockItem> findByStoreItemId(long storeItemId);


//    @Query(value = "select si from StockItem si where si.storeItem.storeItemId = ?1 and si.size = ?2")
//    StockItem findByStoreItemIdAndSize(long storeItemId, String size);

}