package com.example.itbangmodkradankanbanapi.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
public class ErrorResponse {
    private final Timestamp timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
}