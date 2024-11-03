
package com.example.itbangmodkradankanbanapi.controllers.V3;

import com.example.itbangmodkradankanbanapi.dtos.V3.task.FormTaskDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.RequestRemoveFilesDto;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.TaskDtoV3;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.services.V3.TaskServiceV3;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @DeleteMapping("{nanoId}/tasks/{taskId}/attachment")
    public ResponseEntity<Object> DeleteAttachments(@RequestBody RequestRemoveFilesDto requestRemoveFilesDto) throws IOException {
        return ResponseEntity.ok(service.deleteAttachments(requestRemoveFilesDto));
    }

    @GetMapping("{nanoId}/tasks/{tasksId}/attachment/{fileId}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Integer tasksId, @PathVariable String fileId) throws IOException {
        ResponseEntity<Resource> fileResource = service.getAttachment(tasksId, fileId);

        String contentDisposition = fileResource.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
        String contentType = fileResource.getHeaders().getContentType() != null ?
                fileResource.getHeaders().getContentType().toString() :
                MediaType.APPLICATION_OCTET_STREAM_VALUE;


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(fileResource.getBody());
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
