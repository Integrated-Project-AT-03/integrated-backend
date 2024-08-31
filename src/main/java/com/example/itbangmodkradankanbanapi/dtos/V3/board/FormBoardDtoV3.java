package com.example.itbangmodkradankanbanapi.dtos.V3.board;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FormBoardDtoV3 {
    @NotNull @Size(max = 120)
    private String name;
    @NotNull
    private String ownerOid;
}
