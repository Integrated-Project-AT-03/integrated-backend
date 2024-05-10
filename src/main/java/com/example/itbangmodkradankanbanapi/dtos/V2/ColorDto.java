package com.example.itbangmodkradankanbanapi.dtos.V2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ColorDto {
    private Integer id;
    private String name;

    private String hex;

    public String getHex(){
        return "#"+hex;
    }


}
