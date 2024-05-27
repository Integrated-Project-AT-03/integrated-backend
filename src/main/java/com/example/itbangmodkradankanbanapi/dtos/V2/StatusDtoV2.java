package com.example.itbangmodkradankanbanapi.dtos.V2;

import com.example.itbangmodkradankanbanapi.entities.V1.Task;
import com.example.itbangmodkradankanbanapi.entities.V2.TasksV2;
import com.example.itbangmodkradankanbanapi.utils.EmptyToNullAndTrimDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@Data
public class StatusDtoV2 {
    private Integer id;
    private String name;
    @JsonProperty("description")
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    private String statusDescription;
    private String colorHex;
    @JsonIgnore
    private List<TasksV2> tasks;
    public Integer getNumOfTask(){
        return tasks == null ? 0 : tasks.size();
    }
    public String getColorHex(){
        return "#"+colorHex;
    }


}
