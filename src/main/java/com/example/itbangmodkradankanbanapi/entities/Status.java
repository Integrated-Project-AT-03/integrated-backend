package com.example.itbangmodkradankanbanapi.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;

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
    @OneToMany(mappedBy = "statusByIdstatus")
    private Collection<Tasks> tasksByIdstatus;

}
