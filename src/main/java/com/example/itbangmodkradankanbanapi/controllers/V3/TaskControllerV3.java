
package com.example.itbangmodkradankanbanapi.controllers.V3;

import com.example.itbangmodkradankanbanapi.dtos.V3.task.FormTaskDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.FullTaskDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.RequestRemoveFilesDto;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.TaskDtoV3;
import com.example.itbangmodkradankanbanapi.entities.V3.TaskAttachment;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.services.V3.TaskServiceV3;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "Find a specific task by ID", description = "Fetches the details of a specific task by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the task",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullTaskDtoV3.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("{nanoId}/tasks/{id}")
    public ResponseEntity<Object> findTask(@PathVariable String nanoId, @PathVariable @NotNull Integer id) {
        return ResponseEntity.ok(service.getTask(id));
    }

    @Operation(summary = "Delete a specific task by ID", description = "Deletes a task from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDtoV3.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("{nanoId}/tasks/{id}")
    public TaskDtoV3 deleteTask(@PathVariable String nanoId, @PathVariable Integer id) {
        return service.deleteTask(id);
    }

    @Operation(summary = "Update a specific task by ID", description = "Updates a task's details in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDtoV3.class))),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("{nanoId}/tasks/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable String nanoId, @PathVariable Integer id, @RequestBody @Valid FormTaskDtoV3 task) {
        return ResponseEntity.ok(service.updateTask(id, task));
    }

    @Operation(summary = "Create a new task", description = "Adds a new task to the board.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDtoV3.class))),
            @ApiResponse(responseCode = "404", description = "Board not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("{nanoId}/tasks")
    public ResponseEntity<Object> addTask(@RequestBody @Valid FormTaskDtoV3 task, @PathVariable String nanoId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addTask(task, nanoId));
    }

    @Operation(summary = "Upload attachments to a task", description = "Uploads one or more attachments to a specific task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachments uploaded successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskAttachment.class)))),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "409", description = "File name conflict")
    })
    @PutMapping("{nanoId}/tasks/{id}/attachment")
    public ResponseEntity<Object> uploadAttachment(@PathVariable Integer id,@PathVariable String nanoId, @RequestParam("files") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok(service.uploadAttachment(files, id));
    }

    @Operation(summary = "Delete attachments from a task", description = "Deletes specified attachments from a task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachments deleted successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskAttachment.class)))),
            @ApiResponse(responseCode = "404", description = "Task or attachment not found")
    })
    @DeleteMapping("{nanoId}/tasks/{id}/attachment")
    public ResponseEntity<Object> deleteAttachments(@PathVariable Integer id,@PathVariable String nanoId, @RequestBody RequestRemoveFilesDto requestRemoveFilesDto) throws IOException {
        return ResponseEntity.ok(service.deleteAttachments(requestRemoveFilesDto));
    }

    @Operation(summary = "Download a specific attachment", description = "Fetches a specific attachment from a task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment downloaded successfully",
                    content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "Attachment not found")
    })
    @GetMapping("{nanoId}/tasks/{tasksId}/attachment/{fileId}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable String nanoId, @PathVariable Integer tasksId, @PathVariable String fileId) throws IOException {
        ResponseEntity<Resource> fileResource = service.getAttachment(tasksId, fileId);
        String contentDisposition = fileResource.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);

        String fileType = fileResource.getHeaders().getContentType() != null ?
                fileResource.getHeaders().getContentType().toString().toLowerCase() :
                MediaType.APPLICATION_OCTET_STREAM_VALUE;

        MediaType contentType;
        if (fileType.endsWith("png")) {
            contentType = MediaType.IMAGE_PNG;
        } else if (fileType.endsWith("jpg") || fileType.endsWith("jpeg")) {
            contentType = MediaType.IMAGE_JPEG;
        } else {
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        }


        return ResponseEntity.ok()
                .contentType(contentType)
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
