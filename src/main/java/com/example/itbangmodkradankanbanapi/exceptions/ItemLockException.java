package com.example.itbangmodkradankanbanapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class ItemLockException extends ResponseStatusException {
    public ItemLockException(String message) {
        super(HttpStatus.BAD_REQUEST,message);
    }
}
