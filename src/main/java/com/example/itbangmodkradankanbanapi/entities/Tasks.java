package com.example.itbangmodkradankanbanapi.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Data
@Table(name = "tasks")
public class Tasks {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idTask")
    private String idTask;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "Description")
    private String description;
    @Basic
    @Column(name = "createdOn")
    private Timestamp createdOn;
    @Basic
    @Column(name = "updatedOn")
    private Timestamp updatedOn;
    @OneToMany(mappedBy = "tasksByIdTasks")
    private Collection<Assignees> assigneesByIdTask;
    @ManyToOne
    @JoinColumn(name = "idstatus", referencedColumnName = "idstatus", nullable = false)
    private Status statusByIdstatus;

}
