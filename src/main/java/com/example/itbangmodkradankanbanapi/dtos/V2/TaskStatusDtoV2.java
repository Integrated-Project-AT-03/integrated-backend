package com.example.itbangmodkradankanbanapi.dtos.V2;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskStatusDtoV2 {
    private  Integer id;
    @JsonProperty("name")
    private String statusName;
    private String colorHex;
    public String getColorHex(){
        return "#"+colorHex;
    }
}
