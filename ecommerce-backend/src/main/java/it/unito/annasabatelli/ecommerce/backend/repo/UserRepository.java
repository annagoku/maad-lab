package it.unito.annasabatelli.ecommerce.backend.repo;

import it.unito.annasabatelli.ecommerce.backend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    //l'interfaccia fornisce tutti i metodi CRUD per la tabella

    User findByEmail (String mail);

    List<User>  findAll();




}