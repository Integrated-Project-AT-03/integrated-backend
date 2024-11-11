
package com.example.itbangmodkradankanbanapi.controllers.V3;

import com.example.itbangmodkradankanbanapi.dtos.V3.collaborator.*;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.services.V3.CollaboratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/v3")

public class CollaboratorController {
    @Autowired
private CollaboratorService service;
    @Operation(summary = "Get all collaborators by board nanoId", description = "Fetches all collaborators for a specific board.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved collaborators",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CollaboratorDto.class)))),
            @ApiResponse(responseCode = "404", description = "Board not found")
    })
    @GetMapping("boards/{nanoId}/collabs")
    public ResponseEntity<Object> getAllCollaboratorByNanoId(@PathVariable String nanoId) {
        return ResponseEntity.ok(service.getAllCollaboratorByNanoId(nanoId));
    }

    @Operation(summary = "Get specific collaborator by board nanoId and collaborator oid", description = "Fetches a specific collaborator by nanoId and oid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved collaborator",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollaboratorDto.class))),
            @ApiResponse(responseCode = "404", description = "Collaborator or board not found")
    })
    @GetMapping("boards/{nanoId}/collabs/{oid}")
    public ResponseEntity<Object> getCollaboratorByNanoIdAndOid(@PathVariable String nanoId, @PathVariable String oid) {
        return ResponseEntity.ok(service.getCollaboratorByNanoIdAndOid(nanoId, oid));
    }

    @Operation(summary = "Update invite access rights", description = "Updates the access rights for a specific invite.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access rights updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateInviteCollaboratorDto.class))),
            @ApiResponse(responseCode = "404", description = "Collaborator or board not found"),
            @ApiResponse(responseCode = "403", description = "No access to update invite")
    })
    @PatchMapping("boards/{nanoId}/invite/{oid}")
    public ResponseEntity<Object> updateInviteCollab(@PathVariable String nanoId, @PathVariable String oid,@RequestBody UpdateInviteCollaboratorDto form) {
        return ResponseEntity.ok(service.updateInviteCollab(nanoId, oid,form));
    }

    @Operation(summary = "Get all collaborators for the authenticated user", description = "Fetches all collaborators for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved collaborators",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CollaboratorBoardDto.class))))
    })
    @GetMapping("collabs")
    public ResponseEntity<Object> getAllCollaborator(HttpServletRequest request) {
        return ResponseEntity.ok(service.getAllCollaborator(request));
    }


//    @GetMapping("collabs/invite")
//    public ResponseEntity<Object> getAllInvite(HttpServletRequest request) {
//        return ResponseEntity.ok(service.getAllInviteBoard(request));
//    }

    @Operation(summary = "Remove a collaborator from a board", description = "Removes a specific collaborator from a board.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Collaborator successfully removed"),
            @ApiResponse(responseCode = "404", description = "Collaborator or board not found"),
            @ApiResponse(responseCode = "403", description = "No access to remove collaborator")
    })
    @DeleteMapping("boards/{nanoId}/collabs/{oid}")
    public ResponseEntity<Object> removeCollaborator(HttpServletRequest request, @PathVariable String nanoId, @PathVariable String oid) {
        service.removeCollaborator(request, nanoId, oid);
        return ResponseEntity.ok("This collaborator is removed");
    }

    @Operation(summary = "Update collaborator access rights", description = "Updates the access rights for a specific collaborator on a board.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access rights updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateAccessCollaboratorDto.class))),
            @ApiResponse(responseCode = "404", description = "Collaborator or board not found"),
            @ApiResponse(responseCode = "403", description = "No access to update collaborator")
    })
    @PatchMapping("boards/{nanoId}/collabs/{oid}")
    public ResponseEntity<Object> updateAccessRight(@PathVariable String nanoId, @PathVariable String oid, @Valid @RequestBody UpdateAccessCollaboratorDto form) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateAccessBoard(nanoId, oid, form));
    }

    @Operation(summary = "Invite a new collaborator to a board", description = "Invite a new collaborator to a specific board.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Collaborator Invited successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestCollaboratorDto.class))),
            @ApiResponse(responseCode = "404", description = "Board not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - collaborator already exists or is the board owner"),
            @ApiResponse(responseCode = "403", description = "No access to add collaborator")
    })
    @PostMapping("boards/{nanoId}/collabs")
    public ResponseEntity<Object> InviteCollaborator(HttpServletRequest request, @PathVariable String nanoId, @Valid @RequestBody FormCollaboratorDto form) {
        return ResponseEntity.ok(service.inviteCollaborator(request, nanoId, form));
    }
    @Operation(summary = "Cancel invite (Owner)", description = "Cancel invite from request collaborator.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Collaborator cancel invite successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestCollaboratorDto.class))),
            @ApiResponse(responseCode = "404", description = "invite not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - collaborator already exists or is the board owner"),
            @ApiResponse(responseCode = "403", description = "No access to cancel invite collaborator")
    })
    @DeleteMapping("boards/{nanoId}/invite/{oid}")
    public ResponseEntity<Object> cancelCollaborator(@PathVariable String nanoId, @PathVariable String oid) {
        return ResponseEntity.ok(service.cancelInviteCollaborator(nanoId, oid));
    }

    @Operation(summary = "Receive invite", description = "Receive invite from request collaborator.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Collaborator receive successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestCollaboratorDto.class))),
            @ApiResponse(responseCode = "404", description = "Board or User not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - collaborator already exists or is the board owner"),
            @ApiResponse(responseCode = "403", description = "No access to add collaborator")
    })


    @PostMapping("collabs/receive-invite")
    public ResponseEntity<Object> receiveCollaborator(HttpServletRequest request,  @RequestBody FormReceiveCollaboratorDto form) {
        return ResponseEntity.ok(service.receiveCollaborator(request,form));
    }


    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFound(ItemNotFoundException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(Timestamp.from(Instant.now()),HttpStatus.NOT_FOUND.value(),null, ex.getReason(), request.getDescription(false).substring(4));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
    }
}
