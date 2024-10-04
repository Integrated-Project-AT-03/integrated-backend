package com.example.itbangmodkradankanbanapi.dtos.V3.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAccessCollaboratorDto {
    @NotNull
    @NotBlank
    private String accessRight;
}


