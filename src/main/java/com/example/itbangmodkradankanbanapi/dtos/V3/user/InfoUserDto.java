package com.example.itbangmodkradankanbanapi.dtos.V3.user;

import com.example.itbangmodkradankanbanapi.entities.user.UserdataEntity;
import lombok.Data;

@Data
public class InfoUserDto {
    private String oid;
    private String name;
    private String email;
    private UserdataEntity.RoleEnum role;
}
