package com.example.itbangmodkradankanbanapi.dtos;

import com.example.itbangmodkradankanbanapi.entities.TasksV2;
import com.example.itbangmodkradankanbanapi.entities.TypeColor;
import com.example.itbangmodkradankanbanapi.utils.EmptyToNullAndTrimDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class StatusDtoV2 {
    private Integer id;
    @JsonProperty("name")
    private String statusName;
    @JsonProperty("description")
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    private String statusDescription;
    @Enumerated(EnumType.STRING)
    private TypeColor hex;
}