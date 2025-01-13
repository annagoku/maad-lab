package it.unito.annasabatelli.ecommerce.backend.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unito.annasabatelli.ecommerce.backend.model.redis.ShoppingCart;
import it.unito.annasabatelli.ecommerce.backend.model.redis.ShoppingCartItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public ShoppingCart getCart(String email) throws JsonProcessingException {
        String json = redisTemplate.opsForValue().get(ShoppingCart.REDIS_KEY_PREFIX+email);
        log.info("JSON LETTO DA REDIS: "+json);
        if(json == null) {
            return null;
        }
        else {
            ObjectMapper mapper = new ObjectMapper();
            ShoppingCart c = mapper.readValue(json, ShoppingCart.class);
            log.info("CARRELLO: "+c);
            return c;

        }
    }

    public boolean deleteCart(String email) {
        return redisTemplate.delete(ShoppingCart.REDIS_KEY_PREFIX+email);
    }
    public ShoppingCart addItem(String email, ShoppingCartItem ci) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = redisTemplate.opsForValue().get(ShoppingCart.REDIS_KEY_PREFIX+email);
        ShoppingCart c = null;
        if(json == null) {
            c = new ShoppingCart();
            c.setUserId(email);
        }
        else {
            c = mapper.readValue(json, ShoppingCart.class);
        }
        c.addItem(ci);
        json = mapper.writeValueAsString(c);
        redisTemplate.opsForValue().set(c.redisKey(), json);
        return c;
    }

    public ShoppingCart saveCart(ShoppingCart shoppingCart) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(shoppingCart);
        redisTemplate.opsForValue().set(shoppingCart.redisKey(), json);
        return shoppingCart;
    }

}
