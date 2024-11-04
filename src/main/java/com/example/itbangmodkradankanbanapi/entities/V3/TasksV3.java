package com.example.itbangmodkradankanbanapi.entities.V3;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tasksV3")
public class TasksV3 {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_task")
    private Integer idTask;
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

    @OneToMany(mappedBy = "task")
    private List<TaskAttachment> tasksAttachment ;

    @Basic
    @Column(name = "assignees")
    private String assignees;
    @ManyToOne
    @JoinColumn(name = "id_status", referencedColumnName = "id_status", nullable = false)
    private StatusV3 status;
    @ManyToOne
    @JoinColumn(name = "nano_id_board", referencedColumnName = "nano_id_board", nullable = false)
    private Board board;

}
