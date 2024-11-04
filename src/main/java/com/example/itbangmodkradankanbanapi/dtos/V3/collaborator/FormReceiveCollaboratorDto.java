package com.example.itbangmodkradankanbanapi.dtos.V3.collaborator;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FormReceiveCollaboratorDto {
    @NotNull
    @NotBlank
    private String boardNanoId;

    @NotNull
    @NotBlank
    @Schema(description = "Indicates whether to accept or decline the invitation",
            allowableValues = {"ACCEPT", "DECLINE"})
    @Pattern(regexp = "ACCEPT|DECLINE", message = "Receive Invite must be either 'ACCEPT' or 'DECLINE'")
    private String receiveInvite;
}


