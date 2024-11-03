package com.example.itbangmodkradankanbanapi.dtos.V3.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class FullTaskDtoV3 {
    private Integer id;
    private String title;
    private  String assignees;
    private  String description;
    private TaskStatusDtoV3 status;
    private String boardNanoId;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private Timestamp createdOn;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private Timestamp updatedOn;

    private List<TaskAttachmentDto> tasksAttachment;

}
