
package com.example.itbangmodkradankanbanapi.controllers.V3;

import com.example.itbangmodkradankanbanapi.dtos.V3.status.FormStatusDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.status.StatusDtoV3;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.services.V3.StatusServiceV3;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.sql.Timestamp;
import java.time.Instant;


@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v3/boards")
public class StatusControllerV3 {
    @Autowired
private StatusServiceV3 service;

    @GetMapping("{nanoId}/statuses/{id}")
    public ResponseEntity<Object> findStatus(@PathVariable @NotNull  Integer id){
        return ResponseEntity.ok(service.getStatus(id));
    }

    @DeleteMapping("{nanoId}/statuses/{id}")
    public StatusDtoV3 deleteTask(@PathVariable @NotNull  Integer id, @PathVariable @NotNull String nanoIdBoard)  {
      return   service.deleteStatus(id,nanoIdBoard);
    }

    @PutMapping("{nanoId}/statuses/{id}")
    public  ResponseEntity<Object> updateStatus(@PathVariable  Integer id ,@RequestBody @Valid FormStatusDtoV3 status,@PathVariable String nanoId){
        return ResponseEntity.ok( service.updateStatus(id,status,nanoId));
    }

    @DeleteMapping("{nanoId}/statuses/{deletedId}/{changeId}")
    public ResponseEntity<Object> changeTaskStatus(@PathVariable @NotNull Integer deletedId,@PathVariable @NotNull Integer changeId,@PathVariable @NotNull String nanoId){
        return ResponseEntity.ok(service.ChangeTasksByStatusAndDelete(deletedId,changeId,nanoId));
    }

    @PostMapping("{nanoId}/statuses")
    public ResponseEntity<Object> addStatus(@RequestBody @Valid FormStatusDtoV3 status,@PathVariable String nanoId){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addStatus(status,nanoId));
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> ForeignKeyConflict(DataIntegrityViolationException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.INTERNAL_SERVER_ERROR.value(), null, ex.getMessage(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
    }
    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlerMethodValidationException(
            HandlerMethodValidationException ex,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(null,HttpStatus.BAD_REQUEST.value(),null,"Validation error. Check 'errors' field for details. statusForCreateOrUpdate",  request.getDescription(false));
        for (ParameterValidationResult fieldError : ex.getAllValidationResults()) {
            errorResponse.addValidationError(fieldError.getMethodParameter().getParameter().getName(),fieldError.getResolvableErrors().get(0).getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(null,HttpStatus.BAD_REQUEST.value(),null,"Validation error. Check 'errors' field for details. statusForCreateOrUpdate",  request.getDescription(false));
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }




}
