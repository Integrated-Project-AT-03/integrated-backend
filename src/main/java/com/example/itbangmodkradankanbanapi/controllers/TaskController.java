
package com.example.itbangmodkradankanbanapi.controllers;
import com.example.itbangmodkradankanbanapi.entities.Task;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
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
@RequestMapping("/v1/tasks")
public class TaskController {
    @Autowired
private TaskService service;

    @GetMapping("{id}")
    public ResponseEntity<Object> findTask(@PathVariable String id){
        return ResponseEntity.ok(service.getTask(id));
    }

    @GetMapping("")
    public ResponseEntity<Object> gatAllTask(){
        return  ResponseEntity.ok(service.getAllTask());
    }

    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable String id){
         service.deleteTask(id);
    }


    @PostMapping("")
    @Transactional
    public Task addEmployee(@RequestBody Task task){
        return service.addTask(task);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFound(Exception ex, WebRequest request) {
       String id = request.getDescription(false).split("/")[4];
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.NOT_FOUND.value(),"Not Found", "Task "+ id + " dose not exist !!!!", request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }
}
