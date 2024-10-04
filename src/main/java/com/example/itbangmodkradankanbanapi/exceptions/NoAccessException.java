package com.example.itbangmodkradankanbanapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoAccessException extends ResponseStatusException {
    public NoAccessException(String message) {
        super(HttpStatus.FORBIDDEN,message);
    }
}
