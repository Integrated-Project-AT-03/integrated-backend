package com.example.itbangmodkradankanbanapi.dtos.V3.status;

import com.example.itbangmodkradankanbanapi.utils.EmptyToNullAndTrimDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class FormStatusDtoV3 {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;
    @Size( max = 50)
    @NotNull
    @NotBlank
    @JsonProperty("name")
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    private String name;
    @Size(max = 200)
    @JsonProperty("description")
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    private String statusDescription;
    private Integer colorId = 1;
    @NotNull
    @NotBlank
    private String boardNanoId ;
}
