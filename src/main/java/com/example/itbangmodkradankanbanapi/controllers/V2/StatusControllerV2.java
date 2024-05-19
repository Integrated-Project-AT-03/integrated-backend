
package com.example.itbangmodkradankanbanapi.controllers.V2;

import com.example.itbangmodkradankanbanapi.dtos.V2.FormStatusDtoV2;

import com.example.itbangmodkradankanbanapi.dtos.V2.StatusDtoV2;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.ItemLockException;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.exceptions.ItemRelationException;
import com.example.itbangmodkradankanbanapi.services.V2.StatusServiceV2;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.sql.Timestamp;
import java.time.Instant;


@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v2/statuses")
public class StatusControllerV2 {
    @Autowired
private StatusServiceV2 service;

    @GetMapping("{id}")
    public ResponseEntity<Object> findStatus(@PathVariable @NotNull  Integer id){
        return ResponseEntity.ok(service.getStatus(id));
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllStatus(){
        return  ResponseEntity.ok(service.getAllStatus());
    }

    @DeleteMapping("{id}")
    public StatusDtoV2 deleteTask(@PathVariable @NotNull  Integer id)  {
      return   service.deleteStatus(id);
    }

    @PutMapping("{id}")
    public  ResponseEntity<Object> updateTask(@PathVariable Integer id ,@RequestBody @Valid FormStatusDtoV2 status){
        return ResponseEntity.ok( service.updateStatus(id,status));
    }

    @DeleteMapping("{deletedId}/{changeId}")
    public ResponseEntity<Object> changeTaskStatus(@PathVariable @NotNull Integer deletedId,@PathVariable @NotNull Integer changeId){
        return ResponseEntity.ok(service.ChangeTasksByStatusAndDelete(deletedId,changeId));
    }

    @PostMapping("")
    public ResponseEntity<Object> addStatus(@RequestBody @Valid FormStatusDtoV2 status){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addStatus(status));
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFound(ItemNotFoundException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.NOT_FOUND.value(),null, ex.getReason(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }

    @ExceptionHandler(ItemLockException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleItemNotFound(ItemLockException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.UNPROCESSABLE_ENTITY.value(),null, ex.getReason(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(er);
    }

    @ExceptionHandler(ItemRelationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> itemRelationException(ItemRelationException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.BAD_REQUEST.value(), null, ex.getMessage(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> ForeignKeyConflict(DataIntegrityViolationException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.INTERNAL_SERVER_ERROR.value(), null, ex.getMessage(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(null,HttpStatus.BAD_REQUEST.value(),null,"Validation error. Check 'errors' field for details. statusForCreateOrUpdate",  request.getDescription(false));
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage(),null);
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }





// @ExceptionHandler(HandlerMethodValidationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<ErrorResponse> handlerMethodValidationException(
//            HandlerMethodValidationException ex,
//            WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(null,HttpStatus.BAD_REQUEST.value(),null,ex.getMessage(),  request.getDescription(false));
//        for (ParameterValidationResult fieldError : ex.getAllValidationResults()) {
//            errorResponse.addValidationError(fieldError.getMethodParameter().getParameterName(),null,(String[])
//                    fieldError.getResolvableErrors().stream().map(MessageSourceResolvable::getDefaultMessage).toArray());
//        }
//        return ResponseEntity.badRequest().body(errorResponse);
//    }



}
