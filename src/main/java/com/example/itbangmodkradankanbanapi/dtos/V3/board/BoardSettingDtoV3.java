package com.example.itbangmodkradankanbanapi.dtos.V3.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BoardSettingDtoV3 {
    private String nanoIdBoard ;
    private Boolean enableLimitsTask;
    private Integer limitsTask;
    @JsonIgnore
    private Boolean isPublic;

    public String getVisibility(){
        return isPublic ? "PUBLIC" : "PRIVATE";
    }

}
