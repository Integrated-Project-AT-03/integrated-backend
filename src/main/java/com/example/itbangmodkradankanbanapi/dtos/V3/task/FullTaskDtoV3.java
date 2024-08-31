package com.example.itbangmodkradankanbanapi.dtos.V3.task;

import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class FullTaskDtoV3 {
    private Integer id;
    private String title;
    private  String assignees;
    private  String description;
    private TaskStatusDtoV3 status;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private Timestamp createdOn;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private Timestamp updatedOn;
//    private Board board;

}
