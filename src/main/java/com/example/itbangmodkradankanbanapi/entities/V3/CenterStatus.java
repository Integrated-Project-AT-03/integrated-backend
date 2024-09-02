package com.example.itbangmodkradankanbanapi.entities.V3;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "center_status", schema = "karban")
@Data
public class CenterStatus {
    @Id
    @Column(name = "id_status")
    private Integer id;

    @Basic
    @Column(name = "enable_config")
    private Boolean enableConfig;


    @OneToOne
    @MapsId // This tells JPA to use the same ID as the `StatusV3` entity
    @JoinColumn(name = "id_status", referencedColumnName = "id_status", nullable = false)
    private StatusV3 status;


}
