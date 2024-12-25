package it.unito.annasabatelli.ecommerce.backend.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * Inizializzazione dei dati se il DB è vuoto
 */
@Slf4j
@Component
public class InitDataContextListener  {

    @EventListener(classes = { ApplicationReadyEvent.class })
    public void initMethod() {


    }
}
