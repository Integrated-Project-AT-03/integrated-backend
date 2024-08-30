package com.example.itbangmodkradankanbanapi.entities.V2;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "statusV2")
public class StatusV2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status")
    private  Integer id;
    @Column(name = "status_name")
    private String name;
    @Basic
    @Column(name = "status_description")
    private String statusDescription;
    @ManyToOne
    @JoinColumn(name = "id_color", referencedColumnName = "id_color", nullable = false)
    private ColorStatus color;
    @OneToMany(mappedBy = "status", fetch = FetchType.EAGER) // แก้เพราะ เอา Lazy load ของ jpa ออก
    private List<TasksV2> tasks;
    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;

}
