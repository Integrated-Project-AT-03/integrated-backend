package com.example.itbangmodkradankanbanapi.entities.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users", schema = "itbkk_shared")
public class UserdataEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "oid")
    private String oid;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private RoleEnum role;


    public enum RoleEnum {
        LECTURER,
        STAFF,
        STUDENT;
    }
}
