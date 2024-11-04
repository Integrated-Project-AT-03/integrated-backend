
package com.example.itbangmodkradankanbanapi.controllers.V3;

import com.example.itbangmodkradankanbanapi.Auth.JwtTokenUtil;
import com.example.itbangmodkradankanbanapi.dtos.V3.board.*;
import com.example.itbangmodkradankanbanapi.dtos.V3.status.StatusDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.TaskDtoV3;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.services.V3.BoardService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Operation(summary = "Find a specific board by nanoId", description = "This endpoint fetches the details of a specific board by its nanoId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the board",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullBoardDtoV3.class))),
            @ApiResponse(responseCode = "404", description = "Board not found")
    })
    @GetMapping("{nanoId}")
    public ResponseEntity<Object> findTask(
            @Parameter(description = "The unique ID of the board") @PathVariable String nanoId,
            HttpServletRequest request) {
        return ResponseEntity.ok(service.getBoard(nanoId, request));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the boards",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BoardDtoV3.class))))
    })
    @Operation(summary = "Get all boards", description = "Fetches all boards for the authenticated user.")
    @GetMapping("")
    public ResponseEntity<Object> getAllBoards(HttpServletRequest request) {
        return ResponseEntity.ok(service.getAllBoard(request));
    }

    @Operation(summary = "Get board settings", description = "Fetches the settings of a specific board by nanoId.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved board settings",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BoardSettingDtoV3.class)))
    @GetMapping("{nanoId}/settings")
    public ResponseEntity<Object> getBoardSettings(
            @Parameter(description = "The unique ID of the board") @PathVariable String nanoId) {
        return ResponseEntity.ok(service.getBoardSettings(nanoId));
    }

    @Operation(summary = "Update board settings", description = "Updates the settings of a specific board by nanoId.")
    @ApiResponse(responseCode = "200", description = "Board settings updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FormBoardSettingDtoV3.class)))
    @PutMapping("{nanoId}/settings")
    public ResponseEntity<Object> updateBoardSettings(
            @Parameter(description = "The unique ID of the board") @PathVariable String nanoId,
            @Valid @RequestBody FormBoardSettingDtoV3 settingForm) {
        return ResponseEntity.ok(service.updateBoardSettings(nanoId, settingForm));
    }

    @Operation(summary = "Get tasks by filter", description = "Fetches tasks of a board by nanoId and applies filter criteria.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TaskDtoV3.class))))
    @GetMapping("{nanoId}/tasks")
    public ResponseEntity<Object> getAllTaskByFilter(
            @Parameter(description = "The unique ID of the board") @PathVariable String nanoId,
            @RequestParam(defaultValue = "") String[] filterStatuses,
            @RequestParam(defaultValue = "") String[] sortBy,
            @RequestParam(defaultValue = "asc") String[] sortDirection) {
        return ResponseEntity.ok(service.getAllTasksByBoardAndFilter(nanoId, filterStatuses, sortBy, sortDirection));
    }

    @Operation(summary = "Get all statuses by board", description = "Fetches all statuses associated with a board by nanoId.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved statuses",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StatusDtoV3.class))))
    @GetMapping("{nanoId}/statuses")
    public ResponseEntity<Object> getAllStatusesByBoard(
            @Parameter(description = "The unique ID of the board") @PathVariable String nanoId) {
        return ResponseEntity.ok(service.getAllStatusesByBoard(nanoId));
    }

    @Operation(summary = "Create a new board", description = "Creates a new board for the authenticated user.")
    @ApiResponse(responseCode = "201", description = "Board created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BoardDtoV3.class)))
        @PostMapping("")
    public ResponseEntity<Object> createBoard(@RequestBody FormBoardDtoV3 newBoard, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createBoard(request,newBoard));
    }

    @Operation(summary = "Update board visibility", description = "Updates the visibility of a specific board by nanoId.")
    @ApiResponse(responseCode = "200", description = "Board visibility updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FormBoardVisibilityDtoV3.class)))
    @PatchMapping("{nanoId}")
    public ResponseEntity<Object> updateVisibility(
            @Parameter(description = "The unique ID of the board") @PathVariable String nanoId,
            @Valid @RequestBody FormBoardVisibilityDtoV3 updateBoard) {
        return ResponseEntity.ok(service.updateVisibilityBoard(nanoId, updateBoard));
    }

    // Exception Handlers with Swagger Annotations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(null, HttpStatus.BAD_REQUEST.value(), null, "Validation error. Check 'errors' field for details. BoardForCreateOrUpdate", request.getDescription(false));
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFound(NoSuchElementException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFound(ItemNotFoundException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()), HttpStatus.NOT_FOUND.value(), null, ex.getReason(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }
}
