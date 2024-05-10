package com.example.itbangmodkradankanbanapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class ForeignKeyException extends ResponseStatusException {
    public ForeignKeyException(String message) {
        super(HttpStatus.CONFLICT,message);
    }
}