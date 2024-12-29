package it.unito.annasabatelli.ecommerce.backend.controller;


import it.unito.annasabatelli.ecommerce.backend.jparepo.UserRepository;
import it.unito.annasabatelli.ecommerce.backend.model.jpa.User;
import it.unito.annasabatelli.ecommerce.backend.utilities.exception.BusinessLogicException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
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
@RequestMapping("/api/ecommerce/v1/user")
public class UsersController {
    static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    @Autowired // dependency injection
    UserRepository userRepository;


    private void checkUser(String userEmail) {
        User u = userRepository.findByEmail(userEmail);
        if(u == null) {
            throw new ValidationException("L'utente "+userEmail+" non esiste");
        }
    }


    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> users= new ArrayList<>();

        userRepository.findAll().forEach(users::add);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //Recupero informazioni utente da mail
    @GetMapping("/{mail}")
    public ResponseEntity<User> getUserByMail (@NotNull @PathVariable String mail){

        User userByMail=userRepository.findByEmail(mail);

        return new ResponseEntity<>(userByMail, HttpStatus.OK);
    }


   //aggiornamento dati utente recuperato a DB tramite mail
    @PostMapping (value="/{mail}/update")
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

    //Login utente
    @PostMapping (value="/{mail}/login")
    public ResponseEntity<User> login (@NotNull @RequestBody User user) {
        User userToLogin=userRepository.findByEmail(user.getEmail());

        if(userToLogin == null || !userToLogin.getPassword().equals(user.getPassword())) {
            throw new BusinessLogicException("Credenziali errate");
        }

        return new ResponseEntity<>(userToLogin,HttpStatus.OK);
    }



    //Creazione nuovo utente
    @PostMapping(value="/")

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
