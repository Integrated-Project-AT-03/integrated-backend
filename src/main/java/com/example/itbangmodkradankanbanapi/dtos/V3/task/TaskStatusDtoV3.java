package com.example.itbangmodkradankanbanapi.dtos.V3.task;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskStatusDtoV3 {
    private  Integer id;
    @JsonProperty("name")
    private String statusName;
    private String colorHex;
    public String getColorHex(){
        return "#"+colorHex;
    }
}
