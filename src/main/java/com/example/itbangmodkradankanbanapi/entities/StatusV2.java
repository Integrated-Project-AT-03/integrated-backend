package com.example.itbangmodkradankanbanapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "statusV2")
public class StatusV2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status")
    private  int id;
    @Column(name = "status_name")
    private String statusName;
    @Basic
    @Column(name = "status_description")
    private String statusDescription;
    @Basic
    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private TypeColor colorHex;
    @OneToMany(mappedBy = "status")
    private List<TasksV2> tasks;
    @Basic
    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp createdOn;
    @Basic
    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;
}
