package com.example.itbangmodkradankanbanapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ItemRelationException extends ResponseStatusException {
    public ItemRelationException(String message) {
        super(HttpStatus.BAD_REQUEST,message);
    }
}
