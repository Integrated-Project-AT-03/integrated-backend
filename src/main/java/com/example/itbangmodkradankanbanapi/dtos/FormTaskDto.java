package com.example.itbangmodkradankanbanapi.dtos;

import com.example.itbangmodkradankanbanapi.entities.StatusType;
import com.example.itbangmodkradankanbanapi.utils.EmptyToNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Data

public class FormTaskDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;

    private String title;
    @JsonDeserialize(using = EmptyToNull.class)
    private  String assignees;
    @JsonDeserialize(using = EmptyToNull.class)
    private  String description;
    private StatusType status;





}
