package com.example.itbangmodkradankanbanapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class FullTaskDto {
    private Integer idTask;
    private String title;
    private  String assignees;
    private  String description;
    @JsonIgnore
    private  String statusByStatus;
    public Object getStatus(){
            return statusByStatus;
    }
    private Timestamp createdOn;
    private Timestamp updatedOn;


}
