package it.unito.annasabatelli.ecommerce.backend.controller;


import it.unito.annasabatelli.ecommerce.backend.model.User;
import it.unito.annasabatelli.ecommerce.backend.repo.ChartRepository;
import it.unito.annasabatelli.ecommerce.backend.repo.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController // con questa annotation tutti i metodi pubblici risponderanno a chiamate REST
@RequestMapping("/api/ecommerce/v1")
public class EcommerceController {
    static final Logger LOGGER = LoggerFactory.getLogger(EcommerceController.class);
    @Autowired // dependency injection
    UserRepository userRepository;
    @Autowired // dependency injection
    ChartRepository chartRepository;

    //lista di tutti gli utenti
    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> users= new ArrayList<>();

        userRepository.findAll().forEach(users::add);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //Recupero informazioni utente da mail
    @GetMapping("/user/{mail}")
    public ResponseEntity<User> getUserByMail (@NotNull @PathVariable String mail){

        User userByMail=userRepository.findByEmail(mail);

        return new ResponseEntity<>(userByMail, HttpStatus.OK);
    }


   //aggiornamento dati utente recuperato a DB tramite mail
    @PostMapping (value="/user/{mail}/update")
    public ResponseEntity<User> updateUser (@NotNull @PathVariable String mail, @Valid @RequestBody User user) {
        //l'annotation significa che il servizio REST con la post
        //prendo in base alla email e poi aggiorno solo i campi facoltativi
        User userToChange=userRepository.findByEmail(mail);

        if (userToChange == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userToChange.setBirthdate(user.getBirthdate());
        userRepository.save(userToChange);
        return new ResponseEntity<>(userToChange, HttpStatus.OK);
    }




    //Creazione nuovo utente
    @PostMapping(value="/user")

    public ResponseEntity<User> createNewUser(@Valid @RequestBody User user) {


        User userFound=userRepository.findByEmail(user.getEmail());

        //quando un utente viene creato il ruolo di default è student
        if (userFound==null){
            userFound = userRepository.save(user);
            return new ResponseEntity<>(userFound, HttpStatus.CREATED);

        }
        else{
            return new ResponseEntity<>(userFound, HttpStatus.OK);
        }
    }


}
