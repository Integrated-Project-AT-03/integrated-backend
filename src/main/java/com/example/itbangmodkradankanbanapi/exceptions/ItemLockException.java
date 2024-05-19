package com.example.itbangmodkradankanbanapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

//@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ItemLockException extends ResponseStatusException {
    public ItemLockException(String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY,message);
    }
}
