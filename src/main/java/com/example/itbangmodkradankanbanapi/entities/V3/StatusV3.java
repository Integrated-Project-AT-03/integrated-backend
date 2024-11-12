package com.example.itbangmodkradankanbanapi.entities.V3;

import com.example.itbangmodkradankanbanapi.entities.V2.ColorStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "statusV3" , schema = "karban")
public class StatusV3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status")
    private Integer id;
    @Basic
    @Column(name = "status_name")
    private String name;
    @Basic
    @Column(name = "status_description")
    private String statusDescription;
    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @OneToOne(mappedBy = "status")
    private CenterStatus centerStatus;


    @ManyToOne
    @JoinColumn(name = "nano_id_board", referencedColumnName = "nano_id_board", nullable = false)
    private Board board;
    @OneToMany(mappedBy = "status")
    private List<TasksV3> tasks;

    @ManyToOne
    @JoinColumn(name = "id_color", referencedColumnName = "id_color", nullable = false)
    private ColorStatus color;



}
