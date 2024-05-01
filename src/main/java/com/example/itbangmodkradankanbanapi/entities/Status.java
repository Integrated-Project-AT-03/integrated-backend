package com.example.itbangmodkradankanbanapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "status")
public class Status {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_status")
    private Integer id;
    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusType statusType;
    @JsonIgnore
    @OneToMany(mappedBy = "statusByIdStatus")
    private List<Task> tasksByIdStatus;
    }
