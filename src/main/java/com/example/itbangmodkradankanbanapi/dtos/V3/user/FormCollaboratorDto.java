package com.example.itbangmodkradankanbanapi.dtos.V3.user;

import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardsRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class FormCollaboratorDto {
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "WRITE|READ", message = "Visibility must be either 'WRITE' or 'READ'")
    private String accessRight;
}


