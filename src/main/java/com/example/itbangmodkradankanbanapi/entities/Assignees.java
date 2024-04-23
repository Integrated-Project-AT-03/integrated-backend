package com.example.itbangmodkradankanbanapi.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "assigness")
public class Assignees {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idAssignees")
    private String idAssignees;
    @Basic
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "idTasks", referencedColumnName = "idTask", nullable = false)
    private Tasks tasksByIdTasks;

}
