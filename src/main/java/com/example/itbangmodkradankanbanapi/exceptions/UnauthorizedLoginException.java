package com.example.itbangmodkradankanbanapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class UnauthorizedLoginException extends ResponseStatusException {
    public UnauthorizedLoginException(String message) {
        super(HttpStatus.UNAUTHORIZED,message);
    }
}
