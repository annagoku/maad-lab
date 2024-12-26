package it.unito.annasabatelli.ecommerce.backend.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unito.annasabatelli.ecommerce.backend.model.redis.Chart;
import it.unito.annasabatelli.ecommerce.backend.model.redis.ChartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChartService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public Chart getChart(String email) throws JsonProcessingException {
        String json = redisTemplate.opsForValue().get(email);
        if(json == null) {
            return null;
        }
        else {
            ObjectMapper mapper = new ObjectMapper();
            Chart c = mapper.readValue(json, Chart.class);
            return c;

        }
    }

    public Chart addItem(String email, ChartItem ci) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = redisTemplate.opsForValue().get(email);
        Chart c = null;
        if(json == null) {
            c = new Chart();
            c.setUserId(email);
        }
        else {
            c = mapper.readValue(json, Chart.class);
        }
        c.addItem(ci);
        json = mapper.writeValueAsString(c);
        redisTemplate.opsForValue().set(c.getRedisKey(), json);
        return c;
    }

    public Chart saveChart(Chart chart) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(chart);
        redisTemplate.opsForValue().set(chart.getRedisKey(), json);
        return chart;
    }

}
