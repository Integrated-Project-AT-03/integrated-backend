package com.example.itbangmodkradankanbanapi.dtos.V2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskDtoV2 {
    private Integer id;
    private String title;
    private  String assignees;
    @JsonProperty("status")
    private  String statusStatusName;
    private  String statusColorHex;

    public String getStatusColorHex(){
        return "#"+statusColorHex;
    }

}
