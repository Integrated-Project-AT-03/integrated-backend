
package com.example.itbangmodkradankanbanapi.controllers.V2;

import com.example.itbangmodkradankanbanapi.dtos.V2.FormTaskDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V2.TaskDtoV2;
import com.example.itbangmodkradankanbanapi.exceptions.*;
import com.example.itbangmodkradankanbanapi.services.V2.TaskServiceV2;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
                                             @RequestParam(defaultValue = "asc") String[] sortDirection)
    {
        return  ResponseEntity.ok(service.getAllTaskByStatusIdIn(filterStatuses,sortBy,sortDirection));
    }
    @DeleteMapping("{id}")
    public TaskDtoV2 deleteTask(@PathVariable  Integer id){
       return  service.deleteTask(id);
    }
    @PutMapping("{id}")
    public  ResponseEntity<Object> updateTask(@PathVariable  Integer id ,@RequestBody @Valid FormTaskDtoV2 task){
       return ResponseEntity.ok( service.updateTask(id,task));
    }
    @PostMapping("")
    public ResponseEntity<Object> addTask(@RequestBody @Valid FormTaskDtoV2 task){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addTask(task));
    }
    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlerMethodValidationException(
            HandlerMethodValidationException ex,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(null,HttpStatus.BAD_REQUEST.value(),null,"Validation error. Check 'errors' field for details. taskForCreateOrUpdate",  request.getDescription(false));
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
        ErrorResponse errorResponse = new ErrorResponse(null,HttpStatus.BAD_REQUEST.value(),null,"Validation error. Check 'errors' field for details. taskForCreateOrUpdate",  request.getDescription(false));
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
