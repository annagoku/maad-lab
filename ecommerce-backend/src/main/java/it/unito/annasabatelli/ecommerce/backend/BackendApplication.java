package it.unito.annasabatelli.ecommerce.backend;

import lombok.extern.slf4j.Slf4j; //fornisce logger
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

@SpringBootApplication
@Slf4j
public class BackendApplication {

    //adeguamento date alla time zone
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        log.info(TimeZone.getDefault().toString());

        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }



    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
