package com.example.itbangmodkradankanbanapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class InvalidTokenException extends ResponseStatusException {
    public InvalidTokenException(String message) {
        super(HttpStatus.UNAUTHORIZED,message);
    }
}
