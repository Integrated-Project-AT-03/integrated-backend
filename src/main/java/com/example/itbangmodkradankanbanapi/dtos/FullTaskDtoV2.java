package com.example.itbangmodkradankanbanapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class FullTaskDtoV2 {
    private Integer id;
    private String title;
    private  String assignees;
    private  String description;
    @JsonIgnore
    private  String taskStatus;
    public Object getStatus(){
            return taskStatus;
    }
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private Timestamp createdOn;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private Timestamp updatedOn;


}
