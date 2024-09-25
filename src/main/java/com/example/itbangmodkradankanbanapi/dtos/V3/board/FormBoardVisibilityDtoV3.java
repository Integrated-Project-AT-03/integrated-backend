package com.example.itbangmodkradankanbanapi.dtos.V3.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormBoardVisibilityDtoV3 {
    private String visibility ;
    @JsonIgnore
    private Boolean isPublic;
    public Boolean getIsPublic(){
        return visibility.equals("PUBLIC");
    }

    public String getVisibility(){
        return isPublic ? "PUBLIC" : "PRIVATE";
    }



}




