package com.example.itbangmodkradankanbanapi.dtos.V2;


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
