package com.example.itbangmodkradankanbanapi.dtos.V1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskDto {
    private Integer id;
    private String title;
    private  String assignees;

    @JsonProperty("status")
    private  String statusByIdStatusStatus;

}
