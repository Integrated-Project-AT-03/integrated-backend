package com.example.itbangmodkradankanbanapi.dtos.V3.board;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormBoardVisibilityDtoV3 {
    private String visibility ;
    private Boolean isPublic;
    public Boolean getIsPublic(){
        return visibility.equals("PUBLIC");
    }
}




