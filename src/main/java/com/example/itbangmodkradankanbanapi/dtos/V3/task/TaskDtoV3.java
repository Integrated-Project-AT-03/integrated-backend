package com.example.itbangmodkradankanbanapi.dtos.V3.task;

import com.example.itbangmodkradankanbanapi.entities.V3.TaskAttachment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class TaskDtoV3 {
    private Integer id;
    private String title;
    private  String assignees;
    @JsonProperty("status")
    private  String statusStatusName;
    private  String statusColorHex;
    private String boardNanoId;
    @JsonIgnore
    private List<TaskAttachment> tasksAttachment;

    public Integer getNumOfTaskAttachment(){
        return tasksAttachment.size();
    }
    public String getStatusColorHex(){
        return "#"+statusColorHex;
    }

}
