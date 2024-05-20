package com.example.itbangmodkradankanbanapi.exceptions;

import lombok.Data;


@Data
public class ValidationError {
    private final String field;
    private final String message;
}
