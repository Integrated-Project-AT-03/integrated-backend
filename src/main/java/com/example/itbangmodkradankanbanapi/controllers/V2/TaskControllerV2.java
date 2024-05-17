
package com.example.itbangmodkradankanbanapi.controllers.V2;

import com.example.itbangmodkradankanbanapi.dtos.V2.FormTaskDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V2.TaskDtoV2;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.services.V2.TaskServiceV2;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v2/tasks")
public class TaskControllerV2 {
    @Autowired
private TaskServiceV2 service;

    @GetMapping("{id}")
    public ResponseEntity<Object> findTask(@PathVariable @NotNull Integer id){
        return ResponseEntity.ok(service.getTask(id));
    }



    @GetMapping("")
    public ResponseEntity<Object> getAllTask(@RequestParam(defaultValue = "") String[] filterStatuses,
                                            @RequestParam(defaultValue = "") String[] sortBy,
                                             @RequestParam(defaultValue = "ASC") String[] sortDirection)
    {
        return  ResponseEntity.ok(service.getAllTaskByStatusIdIn(filterStatuses,sortBy,sortDirection));
    }
    @DeleteMapping("{id}")
    public TaskDtoV2 deleteTask(@PathVariable @NotNull  Integer id){
       return  service.deleteTask(id);
    }
    @PutMapping("{id}")
    public  ResponseEntity<Object> updateTask(@PathVariable @NotNull  Integer id ,@RequestBody @Valid FormTaskDtoV2 newTask){
       return ResponseEntity.ok( service.updateTask(id,newTask));
    }
    @PostMapping("")
    public ResponseEntity<Object> addTask(@RequestBody @Valid FormTaskDtoV2 task){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addTask(task));
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFound(NoSuchElementException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.NOT_FOUND.value(),"Not Found", ex.getMessage(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFound(ItemNotFoundException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.NOT_FOUND.value(),null, ex.getReason(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(null,HttpStatus.BAD_REQUEST.value(),null,"Validation error. Check 'errors' field for details. taskForCreateOrUpdate",  request.getDescription(false));
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage(),null);
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
