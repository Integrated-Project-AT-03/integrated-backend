package com.example.itbangmodkradankanbanapi.dtos.V2;

import com.example.itbangmodkradankanbanapi.utils.EmptyToNullAndTrimDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class FormTaskDtoV2 {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    @NotNull
    private String title;
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    private  String assignees;
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    private  String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("status")
    private  String statusName;
    @JsonIgnore
    private  String statusColorHex;
    public String getStatusColor(){
        return "#"+statusColorHex;
    }



}
