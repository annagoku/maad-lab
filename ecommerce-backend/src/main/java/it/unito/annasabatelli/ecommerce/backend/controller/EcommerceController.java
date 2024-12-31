package it.unito.annasabatelli.ecommerce.backend.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import it.unito.annasabatelli.ecommerce.backend.jparepo.StockRepository;
import it.unito.annasabatelli.ecommerce.backend.model.jpa.StockItem;
import it.unito.annasabatelli.ecommerce.backend.model.jpa.StoreItem;
import it.unito.annasabatelli.ecommerce.backend.model.redis.ShoppingCart;
import it.unito.annasabatelli.ecommerce.backend.model.redis.ShoppingCartItem;
import it.unito.annasabatelli.ecommerce.backend.model.jpa.User;
import it.unito.annasabatelli.ecommerce.backend.jparepo.StoreRepository;
import it.unito.annasabatelli.ecommerce.backend.jparepo.UserRepository;
import it.unito.annasabatelli.ecommerce.backend.redis.CartService;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
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
    StoreRepository storeRepository;
    @Autowired // dependency injection
    StockRepository stockRepository;
    @Autowired
    CartService cartService;

    /**
     * GET Carrello per utente
     * @return Carrello dell'utente
     */
    @GetMapping("/cart/{mail}")
    public ResponseEntity<ShoppingCart> getCartByMail(@NotNull @PathVariable String mail) throws JsonProcessingException {
        checkUser(mail);

        ShoppingCart c = cartService.getCart(mail);
        if(c == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(c, HttpStatus.OK);

        }
    }

    @PostMapping("/cart/{mail}/add-item")
    public ResponseEntity<ShoppingCart> addItemToCart(@NotNull @PathVariable String mail,
                                                       @NotNull @RequestBody ShoppingCartItem shoppingCartItem) throws JsonProcessingException {
        checkUser(mail);

        ShoppingCart c = cartService.addItem(mail, shoppingCartItem);
        return new ResponseEntity<>(c, HttpStatus.OK);

    }

    @PostMapping("/cart/{mail}")
    public ResponseEntity<ShoppingCart> saveCart(@NotNull @PathVariable String mail,
                                                  @NotNull @RequestBody ShoppingCart shoppingCart) throws JsonProcessingException {
        checkUser(mail);
        if(!mail.equalsIgnoreCase(shoppingCart.getUserId())) {
            throw new ValidationException("userid non coerente con quello all'interno del carrello");
        }
        ShoppingCart c = cartService.saveCart(shoppingCart);
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

    /**
     * lista di tutti gli oggetti dello store
     *
     */
    @GetMapping("/store/{storeItemId}/sizes")
    public ResponseEntity<List<StockItem>> getSizesForStoreItem(@NotNull @PathVariable long storeItemId ) {

        List<StockItem> list = stockRepository.findByStoreItemId(storeItemId);

        if(list.size()>0) {
            return new ResponseEntity<>(list, HttpStatus.OK);

        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
