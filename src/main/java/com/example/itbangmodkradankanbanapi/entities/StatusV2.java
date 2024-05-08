package com.example.itbangmodkradankanbanapi.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;

@Entity
@Data
public class StatusV2 {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "status_name")
    private String statusName;
    @Basic
    @Column(name = "status_description")
    private String statusDescription;
    @OneToMany(mappedBy = "statusV2ByTaskStatus")
    private Collection<TasksV2> tasksV2sByStatusName;
}
