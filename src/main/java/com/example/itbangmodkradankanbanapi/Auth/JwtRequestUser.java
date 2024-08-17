package com.example.itbangmodkradankanbanapi.Auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JwtRequestUser {

    @NotBlank
    private String userName;

    @NotBlank
    private String password;
}
