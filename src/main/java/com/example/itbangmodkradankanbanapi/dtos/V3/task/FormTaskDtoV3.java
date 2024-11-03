package com.example.itbangmodkradankanbanapi.dtos.V3.task;

import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.utils.EmptyToNullAndTrimDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class FormTaskDtoV3 {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String title;
    private String boardNanoId;
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    @Size(max = 30)
    private  String assignees;
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    @Size(max = 500)
    private  String description;
    @JsonProperty("status")
    private  Integer statusId = 1;


    
}
