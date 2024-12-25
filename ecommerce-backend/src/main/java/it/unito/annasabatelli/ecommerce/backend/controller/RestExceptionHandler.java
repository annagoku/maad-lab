package it.unito.annasabatelli.ecommerce.backend.controller;

import it.unito.annasabatelli.ecommerce.backend.utilities.controller.ErrorDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import it.unito.annasabatelli.ecommerce.backend.utilities.exception.BusinessLogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice

public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    protected static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);


    //Gestione eccezioni logiche runtime
    @ExceptionHandler(value = {Exception.class })
    protected ResponseEntity<Object> handleException(
            RuntimeException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails();
        error.setErrorMessage(ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return handleExceptionInternal(ex, error,
                headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    @ExceptionHandler(value = {BusinessLogicException.class })
    protected ResponseEntity<Object> handleBusinessLogicException(
            RuntimeException ex, WebRequest request) {
        LOGGER.error("Business Logic ex: "+ex.getMessage());
        ErrorDetails error = new ErrorDetails();
        error.setErrorMessage(ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return handleExceptionInternal(ex, error,
                headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    //Gestione della validazione dell'input
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {


        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            //struttura della mappa
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorDetails error = new ErrorDetails();
        error.setValidationErrors(errors);
        error.setErrorMessage("Campi di input non validi");


        return new ResponseEntity(error, headers, status);

    }
}