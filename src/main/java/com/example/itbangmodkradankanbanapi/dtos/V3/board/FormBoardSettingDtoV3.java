package com.example.itbangmodkradankanbanapi.dtos.V3.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormBoardSettingDtoV3 {
    private String nanoIdBoard ;
    @NotNull
    private Boolean enableLimitsTask;
    @NotNull
    private Integer limitsTask;
}
