package it.unito.annasabatelli.ecommerce.backend.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unito.annasabatelli.ecommerce.backend.model.jpa.StoreItem;
import it.unito.annasabatelli.ecommerce.backend.model.redis.Chart;
import it.unito.annasabatelli.ecommerce.backend.model.redis.ChartItem;
import it.unito.annasabatelli.ecommerce.backend.model.jpa.User;
import it.unito.annasabatelli.ecommerce.backend.jparepo.StoreRepository;
import it.unito.annasabatelli.ecommerce.backend.jparepo.UserRepository;
import it.unito.annasabatelli.ecommerce.backend.redis.ChartService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    StoreRepository storeRepository;

    @Autowired
    ChartService chartService;

    /**
     * GET Carrello per utente
     * @return Carrello dell'utente
     */
    @GetMapping("/chart/{mail}")
    public ResponseEntity<Chart> getChartByMail(@NotNull @PathVariable String mail) throws JsonProcessingException {
        checkUser(mail);

        Chart c = chartService.getChart(mail);
        if(c == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(c, HttpStatus.OK);

        }
    }

    @PostMapping("/chart/{mail}/add-item")
    public ResponseEntity<Chart> addItemToChart(@NotNull @PathVariable String mail,
                                                @NotNull @RequestBody ChartItem chartItem) throws JsonProcessingException {
        checkUser(mail);

        Chart c = chartService.addItem(mail, chartItem);
        return new ResponseEntity<>(c, HttpStatus.OK);

    }

    @PostMapping("/chart/{mail}")
    public ResponseEntity<Chart> saveChart(@NotNull @PathVariable String mail,
                                                @NotNull @RequestBody Chart chart) throws JsonProcessingException {
        checkUser(mail);
        if(!mail.equalsIgnoreCase(chart.getUserId())) {
            throw new ValidationException("userid non coerente con quello all'interno del carrello");
        }
        Chart c = chartService.saveChart(chart);
        return new ResponseEntity<>(c, HttpStatus.OK);

    }

    private void checkUser(String userEmail) {
        User u = userRepository.findByEmail(userEmail);
        if(u == null) {
            throw new ValidationException("L'utente "+userEmail+" non esiste");
        }
    }

    /**
     * lista di tutti gli oggetti dello store
     *
     */
    @GetMapping("/store")
    public ResponseEntity<List<StoreItem>> getAllStoreItems() {

        List<StoreItem> items= new ArrayList<>();

        storeRepository.findAll().forEach(items::add);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

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
