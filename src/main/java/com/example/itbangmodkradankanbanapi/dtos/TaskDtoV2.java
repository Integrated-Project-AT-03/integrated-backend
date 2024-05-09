package com.example.itbangmodkradankanbanapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TaskDtoV2 {
    private Integer id;
    private String title;
    private  String assignees;
    @JsonIgnore
    private  String statusStatusName;
    public String getStatus(){
            return statusStatusName;
    }
}
