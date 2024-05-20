package com.example.itbangmodkradankanbanapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class NotAllowedException extends ResponseStatusException {
    public NotAllowedException(String message) {
        super(HttpStatus.BAD_REQUEST,message);
    }
}
