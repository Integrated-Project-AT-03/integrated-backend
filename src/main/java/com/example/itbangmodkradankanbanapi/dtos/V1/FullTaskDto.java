package com.example.itbangmodkradankanbanapi.dtos.V1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class FullTaskDto {
    private Integer id;
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
