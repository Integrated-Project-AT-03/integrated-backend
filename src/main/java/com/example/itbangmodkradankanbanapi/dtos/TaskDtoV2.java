package com.example.itbangmodkradankanbanapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskDtoV2 {
    private Integer id;
    private String title;
    private  String assignees;

    @JsonIgnore
    private  String statusStatusHex;

    public String getStatusColor(){
        return "#"+statusStatusHex.substring(2);
    }

    @JsonProperty("status")
    private  String statusStatusName;
}
