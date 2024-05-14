
package com.example.itbangmodkradankanbanapi.controllers.V2;

import com.example.itbangmodkradankanbanapi.dtos.V2.FormTaskDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V2.TaskDtoV2;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.services.V2.TaskServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

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
    public ResponseEntity<Object> findTask(@PathVariable Integer id){
        return ResponseEntity.ok(service.getTask(id));
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllTask(){
        return  ResponseEntity.ok(service.getAllTask());
    }

    @DeleteMapping("{id}")
    public TaskDtoV2 deleteTask(@PathVariable Integer id){
       return  service.deleteTask(id);
    }
    @PutMapping("{id}")
    public  ResponseEntity<Object> updateTask(@PathVariable Integer id ,@RequestBody FormTaskDtoV2 newTask){
       return ResponseEntity.ok( service.updateTask(id,newTask));
    }
    @PostMapping("")
    public ResponseEntity<Object> addTask(@RequestBody FormTaskDtoV2 task){
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
}
