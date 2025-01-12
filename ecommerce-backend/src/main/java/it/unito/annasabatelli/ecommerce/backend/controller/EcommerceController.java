package it.unito.annasabatelli.ecommerce.backend.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import it.unito.annasabatelli.ecommerce.backend.jparepo.OrderRepository;
import it.unito.annasabatelli.ecommerce.backend.jparepo.StockRepository;
import it.unito.annasabatelli.ecommerce.backend.model.dto.OrderRequest;
import it.unito.annasabatelli.ecommerce.backend.model.jpa.*;
import it.unito.annasabatelli.ecommerce.backend.model.redis.ShoppingCart;
import it.unito.annasabatelli.ecommerce.backend.model.redis.ShoppingCartItem;
import it.unito.annasabatelli.ecommerce.backend.jparepo.StoreRepository;
import it.unito.annasabatelli.ecommerce.backend.jparepo.UserRepository;
import it.unito.annasabatelli.ecommerce.backend.redis.CartService;
import it.unito.annasabatelli.ecommerce.backend.utilities.exception.BusinessLogicException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
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
    OrderRepository orderRepository;
    @Autowired
    CartService cartService;

    @Autowired
    private TransactionTemplate transactionTemplate;


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

    @DeleteMapping("/cart/{mail}")
    public ResponseEntity deleteCart(@NotNull @PathVariable String mail) throws JsonProcessingException {
        checkUser(mail);
        boolean b = cartService.deleteCart(mail);
        if(b) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/order/{mail}")
    public ResponseEntity<Order> createOrder(@NotNull @PathVariable String mail, @NotNull @Valid @RequestBody OrderRequest request) {
        User user = checkUser(mail);
        if(!mail.equalsIgnoreCase(request.getUserMail())) {
            throw new ValidationException("userid non coerente con quello all'interno del carrello");
        }
        Order theOrder = new Order();
        theOrder.setUser(user);

        theOrder.setPaymentMethod(request.getPaymentMethod());
        theOrder.setTimeStamp(new Date());
        theOrder.setShippingAddress(request.getShippingAddress());
        theOrder.setShippingCity(request.getShippingCity());
        theOrder.setShippingProvince(request.getShippingProvince());
        theOrder.setShippingZipCode(request.getShippingZipCode());
        theOrder.setItems(new ArrayList<>());

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                for(ShoppingCartItem si: request.getCart().getItems()) {
                    StockItem stock = stockRepository.findById(si.getSize().getStockItemId()).get();

                    //contollo che ci sia ancora la quantità disponibile
                    if(stock.getStockQuantity() < si.getQuantity()) {
                        throw new BusinessLogicException("Quantità non più disponibile su "+si.getStoreItem().getName()+
                                ". Disponibilità "+stock.getStockQuantity()+ " pezzi");
                    }
                    else {
                        stock.setStockQuantity(stock.getStockQuantity()-si.getQuantity());
                        stockRepository.save(stock);
                    }
                    OrderedItem item = new OrderedItem();
                    item.setOrder(theOrder);
                    item.setStoreItem(si.getStoreItem());
                    item.setQuantity(si.getQuantity());
                    item.setPrice(si.getStoreItem().getPrice()*item.getQuantity());
                    item.setSize(si.getSize().getSize());

                    //incremento il prezzo dell'ordine
                    theOrder.setPrice(theOrder.getPrice()+item.getPrice());
                    theOrder.getItems().add(item);
                }

                orderRepository.save(theOrder);

                //cancello il carrello dopo aver emesso l'ordine
                cartService.deleteCart(mail);

            }
        });


        return new ResponseEntity<>(theOrder, HttpStatus.OK);
    }

    @GetMapping("/order/{mail}")
    public ResponseEntity<List<Order>> getOrders(@NotNull @PathVariable String mail) {
        List<Order> orders = orderRepository.findByUserEmail(mail);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    private User checkUser(String userEmail) {
        User u = userRepository.findByEmail(userEmail);
        if(u == null) {
            throw new ValidationException("L'utente "+userEmail+" non esiste");
        }
        return u;
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
