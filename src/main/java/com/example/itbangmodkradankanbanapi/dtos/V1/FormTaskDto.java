package com.example.itbangmodkradankanbanapi.dtos.V1;

import com.example.itbangmodkradankanbanapi.entities.V1.StatusType;
import com.example.itbangmodkradankanbanapi.utils.EmptyToNullAndTrimDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class FormTaskDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    @NotNull
    private String title;
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    private  String assignees;
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    private  String description;
    private StatusType status;





}
