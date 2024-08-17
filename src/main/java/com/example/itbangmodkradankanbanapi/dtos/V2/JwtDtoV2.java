package com.example.itbangmodkradankanbanapi.dtos.V2;

import lombok.Data;

@Data
public class JwtDtoV2 {
    private String access_token;

    public JwtDtoV2(String access_token) {
        this.access_token = access_token;
    }
}
