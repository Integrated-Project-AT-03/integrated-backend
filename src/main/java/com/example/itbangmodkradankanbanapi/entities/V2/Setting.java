package com.example.itbangmodkradankanbanapi.entities.V2;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "setting", schema = "karban")
public class Setting {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_setting")
    private Integer id;
    @Basic
    private String name;
    @Basic
    private String value;
}
