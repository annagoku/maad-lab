package it.unito.annasabatelli.ecommerce.backend.utilities.controller;

import java.util.Map;

public class ErrorDetails {
    Map<String,String>  validationErrors;
    String              errorMessage;

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
