package com.example.itbangmodkradankanbanapi.dtos.V2;

import com.example.itbangmodkradankanbanapi.utils.EmptyToNullAndTrimDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
public class TaskStatusDtoV2 {
    @JsonProperty("name")
    private String statusName;
    private String colorHex;
    public String getColorHex(){
        return "#"+colorHex;
    }
}
