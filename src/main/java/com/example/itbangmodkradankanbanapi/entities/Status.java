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
    @Column(name = "idstatus")
    private String idstatus;
    @Basic
    @Column(name = "status")
    private Object status;
    @JsonIgnore
    @OneToMany(mappedBy = "statusByIdstatus")
    private List<Task> tasksByIdstatus;

}
