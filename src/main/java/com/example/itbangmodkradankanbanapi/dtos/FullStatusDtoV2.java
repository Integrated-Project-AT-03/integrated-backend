package com.example.itbangmodkradankanbanapi.dtos;

import com.example.itbangmodkradankanbanapi.entities.TypeColor;
import com.example.itbangmodkradankanbanapi.utils.EmptyToNullAndTrimDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
public class FullStatusDtoV2 {
    private Integer id;
    @JsonProperty("name")
    private String statusName;
    @JsonProperty("description")
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    private String statusDescription;
    @Enumerated(EnumType.STRING)
    private TypeColor hex;

}
