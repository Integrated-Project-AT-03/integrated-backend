
package com.example.itbangmodkradankanbanapi.controllers;

import com.example.itbangmodkradankanbanapi.entities.Task;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.services.StatusService;
import com.example.itbangmodkradankanbanapi.services.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/v1/status")
public class StatusController {
    @Autowired
private StatusService service;
    @GetMapping("")
    public ResponseEntity<Object> gatAllStatus(){
        return  ResponseEntity.ok(service.getAllStatus());
    }


}
