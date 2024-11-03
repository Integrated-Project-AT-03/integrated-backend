package com.example.itbangmodkradankanbanapi.dtos.V3.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TaskDtoV3 {
    private Integer id;
    private String title;
    private  String assignees;
    @JsonProperty("status")
    private  String statusStatusName;
    private  String statusColorHex;
    private String boardNanoId;
    public String getStatusColorHex(){
        return "#"+statusColorHex;
    }

}
