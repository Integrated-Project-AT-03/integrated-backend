package com.example.itbangmodkradankanbanapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TaskDto {
    private Integer idTask;
    private String title;
    private  String assignees;
    @JsonIgnore
    private  String statusByIdStatusStatus;
    public Object getStatus(){
            return statusByIdStatusStatus;
    }
}
