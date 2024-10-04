package com.example.itbangmodkradankanbanapi.dtos.V3.user;

import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardsRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class FormCollaboratorDto {
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private String accessRight;
}


