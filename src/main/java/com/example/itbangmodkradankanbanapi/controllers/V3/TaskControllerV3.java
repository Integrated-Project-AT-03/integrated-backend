
package com.example.itbangmodkradankanbanapi.controllers.V3;

import com.example.itbangmodkradankanbanapi.dtos.V3.task.FormTaskDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.TaskDtoV3;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.services.V3.TaskServiceV3;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v3/boards")
public class TaskControllerV3 {
    @Autowired
private TaskServiceV3 service;


    @GetMapping("{nanoId}/tasks/{id}")
    public ResponseEntity<Object> findTask(@PathVariable @NotNull Integer id){
        return ResponseEntity.ok(service.getTask(id));
    }

    @DeleteMapping("{nanoId}/tasks/{id}")
    public TaskDtoV3 deleteTask(@PathVariable  Integer id){
       return  service.deleteTask(id);
    }
    @PutMapping("{nanoId}/tasks/{id}")
    public  ResponseEntity<Object> updateTask(@PathVariable  Integer id ,@RequestBody @Valid FormTaskDtoV3 task){
       return ResponseEntity.ok( service.updateTask(id,task));
    }
    @PostMapping("{nanoId}/tasks")
    public ResponseEntity<Object> addTask(@RequestBody @Valid FormTaskDtoV3 task,@PathVariable String nanoId){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addTask(task,nanoId));
    }

    @PutMapping("{nanoId}/tasks/{taskId}/attachment")
    public ResponseEntity<Object> uploadAttachment(@RequestParam("files")List<MultipartFile> files, @PathVariable int taskId) throws IOException {
        return ResponseEntity.ok(service.uploadAttachment(files,taskId));
    }

    @GetMapping("{nanoId}/tasks-attachment/{fileName}")
    public ResponseEntity<Object> uploadAttachment(@PathVariable String fileName) throws IOException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(service.getAttachment(fileName));
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
