package com.example.itbangmodkradankanbanapi.dtos.V2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskDtoV2 {
    private Integer id;
    private String title;
    private  String assignees;
    private  String description;
    private  TaskStatusDtoV2 status;
}
