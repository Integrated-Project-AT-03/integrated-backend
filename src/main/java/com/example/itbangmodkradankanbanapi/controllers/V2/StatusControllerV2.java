
package com.example.itbangmodkradankanbanapi.controllers.V2;

import com.example.itbangmodkradankanbanapi.dtos.V2.StatusDtoV2;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.ForeignKeyException;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.services.V2.StatusServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.NoSuchElementException;


@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v2/statuses")
public class StatusControllerV2 {
    @Autowired
private StatusServiceV2 service;

    @GetMapping("{id}")
    public ResponseEntity<Object> findStatus(@PathVariable Integer id){
        return ResponseEntity.ok(service.getStatus(id));
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllStatus(){
        return  ResponseEntity.ok(service.getAllStatus());
    }

    @DeleteMapping("{id}")
    public StatusDtoV2 deleteTask(@PathVariable Integer id)  {
      return   service.deleteStatus(id);
    }

    @PutMapping("{id}")
    public  ResponseEntity<Object> updateTask(@PathVariable Integer id ,@RequestBody StatusDtoV2 status){
        return ResponseEntity.ok( service.updateStatus(id,status));
    }

    @PostMapping("")
    public ResponseEntity<Object> addStatus(@RequestBody StatusDtoV2 status){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addStatus(status));
    }


    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFound(NoSuchElementException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.NOT_FOUND.value(),"NOT FOUND", ex.getMessage(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFound(ItemNotFoundException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.NOT_FOUND.value(),null, ex.getReason(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ResponseEntity<ErrorResponse> dataIntegrity(DataIntegrityViolationException ex, WebRequest request) {
//        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error", ex.getMessage(), request.getDescription(false).substring(4));
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
//    }


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> ForeignKeyConflict(ForeignKeyException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),ex.getStatusCode().value(),"FOREIGN KEY CONFLICT", ex.getMessage(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(er);
    }





}
