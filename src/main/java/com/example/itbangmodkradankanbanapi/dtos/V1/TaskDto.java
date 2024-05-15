package com.example.itbangmodkradankanbanapi.dtos.V1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TaskDto {
    private Integer id;
    private String title;
    private  String assignees;

    @JsonProperty("status")
    private  String statusByIdStatusStatus;

}
