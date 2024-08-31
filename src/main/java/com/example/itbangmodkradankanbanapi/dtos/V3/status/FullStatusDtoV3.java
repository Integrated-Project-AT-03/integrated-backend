package com.example.itbangmodkradankanbanapi.dtos.V3.status;


import com.example.itbangmodkradankanbanapi.utils.EmptyToNullAndTrimDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class FullStatusDtoV3 {
    private Integer id;
    private String name;
    @JsonProperty("description")
    @JsonDeserialize(using = EmptyToNullAndTrimDeserializer.class)
    private String statusDescription;
    private Integer colorId;
    private String colorHex;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private Timestamp createdOn;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private Timestamp updatedOn;
}
