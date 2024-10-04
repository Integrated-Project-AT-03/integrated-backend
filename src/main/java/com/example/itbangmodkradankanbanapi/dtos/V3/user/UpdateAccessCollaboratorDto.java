package com.example.itbangmodkradankanbanapi.dtos.V3.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateAccessCollaboratorDto {
    @NotNull
    @NotBlank
    @Pattern(regexp = "WRITE|READ", message = "Visibility must be either 'WRITE' or 'READ'")
    private String accessRight;
}


