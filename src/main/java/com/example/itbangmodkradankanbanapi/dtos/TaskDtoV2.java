package com.example.itbangmodkradankanbanapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TaskDtoV2 {
    private Integer id;
    private String title;
    private  String assignees;
    @JsonIgnore
    private  String taskStatus;
    public Object getStatus(){
            return taskStatus;
    }
}
