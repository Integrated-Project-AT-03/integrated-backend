package com.example.itbangmodkradankanbanapi.dtos.V3.status;


import com.example.itbangmodkradankanbanapi.entities.V2.TasksV2;
import com.example.itbangmodkradankanbanapi.entities.V3.TasksV3;
import com.example.itbangmodkradankanbanapi.utils.EmptyToNullAndTrimDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@Data
public class StatusDtoV3 {
    private Integer id;
    private String name;
    @JsonProperty("description")
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    private String statusDescription;
    private String colorHex;
    @JsonIgnore
    private List<TasksV3> tasks;
    public Integer getNumOfTask(){
        return tasks == null ? 0 : tasks.size();
    }
    public String getColorHex(){
        return "#"+colorHex;
    }


}
