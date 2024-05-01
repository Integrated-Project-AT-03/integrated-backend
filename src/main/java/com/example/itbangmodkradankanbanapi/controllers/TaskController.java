
package com.example.itbangmodkradankanbanapi.controllers;
import com.example.itbangmodkradankanbanapi.dtos.FormTaskDto;
import com.example.itbangmodkradankanbanapi.dtos.TaskDto;
import com.example.itbangmodkradankanbanapi.entities.Task;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
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
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v1/tasks")
public class TaskController {
    @Autowired
private TaskService service;

    @GetMapping("{id}")
    public ResponseEntity<Object> findTask(@PathVariable Integer id){
        return ResponseEntity.ok(service.getTask(id));
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllTask(){
        return  ResponseEntity.ok(service.getAllTask());
    }

    @DeleteMapping("{id}")
    public TaskDto deleteTask(@PathVariable Integer id){
       return  service.deleteTask(id);
    }

    @PutMapping("{id}")
    public  ResponseEntity<Object> updateTask(@PathVariable Integer id ,@RequestBody FormTaskDto newTask){
       return ResponseEntity.ok( service.updateTask(id,newTask));
    }


    @PostMapping("")
    @Transactional
    public FormTaskDto addEmployee(@RequestBody FormTaskDto task){
        return service.addTask(task);
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
