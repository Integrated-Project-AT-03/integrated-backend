package com.example.itbangmodkradankanbanapi.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "tasks")
public class Task {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idTask")
    private String idTask;
    @Column(name = "title")
    private String title;
    @Column(name = "Description")
    private String description;
    @Column(name = "createdOn")
    @CreationTimestamp
    private Timestamp createdOn;

    @Column(name = "assignees")
    private String assignees;

    @UpdateTimestamp
    @Column(name = "updatedOn")
    private Timestamp updatedOn;
    @ManyToOne
    @JoinColumn(name = "idstatus", referencedColumnName = "idstatus", nullable = false)
    private Status statusByIdstatus;

}
