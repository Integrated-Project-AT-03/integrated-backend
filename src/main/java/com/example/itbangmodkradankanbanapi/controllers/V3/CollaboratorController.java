
package com.example.itbangmodkradankanbanapi.controllers.V3;

import com.example.itbangmodkradankanbanapi.dtos.V3.board.FormBoardVisibilityDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.user.FormCollaboratorDto;
import com.example.itbangmodkradankanbanapi.dtos.V3.user.UpdateAccessCollaboratorDto;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.services.V3.CollaboratorService;
import com.example.itbangmodkradankanbanapi.services.V3.ShareBoardService;
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

public class CollaboratorController {
    @Autowired
private CollaboratorService service;
    @GetMapping("{nanoId}/collabs")
    public ResponseEntity<Object> getAllCollaboratorByNanoId(@PathVariable String nanoId){
        return ResponseEntity.ok(service.getAllCollaboratorByNanoId(nanoId));
    }

    @DeleteMapping("{nanoId}/collabs/{oid}")
    public ResponseEntity<Object> removeCollaborator(@PathVariable String nanoId,@PathVariable String oid){
        service.removeCollaborator(nanoId,oid);
        return ResponseEntity.ok("This collaborator is removed");
    }

    @PatchMapping ("{nanoId}/collabs/{oid}")
    public ResponseEntity<Object> updateAccessRight(@PathVariable String nanoId,@PathVariable String oid,@Valid @RequestBody UpdateAccessCollaboratorDto form){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.updateVisibilityBoard(nanoId,oid,form));
    }

    @PostMapping("{nanoId}/collabs")
    public ResponseEntity<Object> addCollaborator(@PathVariable String nanoId, @Valid @RequestBody FormCollaboratorDto form){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addCollaborator(nanoId, form));
    }
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFound(NoSuchElementException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.NOT_FOUND.value(),"Not Found", ex.getMessage(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(null,HttpStatus.BAD_REQUEST.value(),null,"Validation error. Check 'errors' field for details. add collaborator or update collaborator",  request.getDescription(false));
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFound(ItemNotFoundException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.NOT_FOUND.value(),null, ex.getReason(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }
}
