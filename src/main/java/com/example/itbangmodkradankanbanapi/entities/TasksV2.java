package com.example.itbangmodkradankanbanapi.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class TasksV2 {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_task")
    private int idTask;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "created_on")
    private Timestamp createdOn;
    @Basic
    @Column(name = "updated_on")
    private Timestamp updatedOn;
    @Basic
    @Column(name = "assignees")
    private String assignees;

    @ManyToOne
    @JoinColumn(name = "task_status", referencedColumnName = "status_name", nullable = false)
    private StatusV2 statusV2ByTaskStatus;

}
