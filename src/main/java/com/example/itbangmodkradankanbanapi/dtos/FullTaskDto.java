package com.example.itbangmodkradankanbanapi.dtos;

import com.example.itbangmodkradankanbanapi.entities.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String statusByIdStatusStatus;
    public Object getStatus(){
            return statusByIdStatusStatus;
    }
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private Timestamp createdOn;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private Timestamp updatedOn;


}
