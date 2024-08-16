package com.example.itbangmodkradankanbanapi.Auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JwtRequestUser {

    @NotBlank
    @Size(max = 50)
    private String userName;
    @Size(min = 8, max = 14)
    @NotBlank
    private String password;
}
