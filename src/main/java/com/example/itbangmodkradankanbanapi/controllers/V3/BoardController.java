
package com.example.itbangmodkradankanbanapi.controllers.V3;

import com.example.itbangmodkradankanbanapi.dtos.V1.FormTaskDto;
import com.example.itbangmodkradankanbanapi.dtos.V1.TaskDto;
import com.example.itbangmodkradankanbanapi.dtos.V3.board.FormBoardDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.FormTaskDtoV3;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.services.V1.TaskService;
import com.example.itbangmodkradankanbanapi.services.V3.BoardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v3/boards")
public class BoardController {
    @Autowired
private BoardService service;

    @GetMapping("{nanoId}")
    public ResponseEntity<Object> findTask(@PathVariable String nanoId){
        return ResponseEntity.ok(service.getBoard(nanoId));
    }
    @GetMapping("")
    public ResponseEntity<Object> getAllBoards(){
        return  ResponseEntity.ok(service.getAllBoard());
    }

    @GetMapping("{nanoId}/tasks")
    public ResponseEntity<Object> getAllTasksByBoard(@PathVariable String nanoId){
        return  ResponseEntity.ok(service.getAllTasksByBoard(nanoId));
    }



    @GetMapping("{nanoId}/statuses")
    public ResponseEntity<Object> getAllStatusesByBoard(@PathVariable String nanoId){
        return  ResponseEntity.ok(service.getAllStatusesByBoard(nanoId));
    }

    @PostMapping("")
    public ResponseEntity<Object> createBoard(@RequestBody @Valid FormBoardDtoV3 newBoard){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createBoard(newBoard));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(null,HttpStatus.BAD_REQUEST.value(),null,"Validation error. Check 'errors' field for details. BoardForCreateOrUpdate",  request.getDescription(false));
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorResponse);
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
