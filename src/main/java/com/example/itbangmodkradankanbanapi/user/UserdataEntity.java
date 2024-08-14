package com.example.itbangmodkradankanbanapi.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "userdata", schema = "user", catalog = "")
public class UserdataEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "oid")
    private String oid;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "role")
    private String role;
}
