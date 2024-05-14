package com.example.itbangmodkradankanbanapi.dtos.V2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskDtoV2 {
    private Integer id;
    private String title;
    private  String assignees;

    @JsonIgnore
    private  String statusColorHex;
    public String getStatusColor(){
        return "#"+statusColorHex;
    }

    @JsonProperty("status")
    private  String statusStatusName;


}
