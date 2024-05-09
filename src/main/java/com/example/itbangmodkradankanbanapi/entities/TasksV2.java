package com.example.itbangmodkradankanbanapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class TasksV2 {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_task")
    private int id;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp createdOn;
    @Basic
    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;
    @Basic
    @Column(name = "assignees")
    private String assignees;

    @ManyToOne
    @JoinColumn(name = "id_status", referencedColumnName = "id_status", nullable = false)
    private StatusV2 status;

}
