package com.example.itbangmodkradankanbanapi.entities.V2;

import com.example.itbangmodkradankanbanapi.models.SettingLockStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "settings")
public class Setting {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "name_of_configure")
    private String nameOfConfigure;
    @Basic
    @Column(name = "value")
    private Integer value;
    @Basic
    @Column(name = "enable")
    private Boolean enable;
}
