package com.example.itbangmodkradankanbanapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidFieldInputException extends ResponseStatusException {
    public InvalidFieldInputException(String field, String message) {
        super(HttpStatus.BAD_REQUEST,field,null,message,null);
    }
}
