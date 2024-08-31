package com.example.itbangmodkradankanbanapi.entities.V3;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "center_status", schema = "karban")
@Data
public class CenterStatus {
    @Basic
    @Column(name = "enable_config")
    private Boolean enableConfig;
    @OneToOne
    @Id
    @JoinColumn(name = "id_status", referencedColumnName = "id_status", nullable = false)
    private StatusV3 statusV3ByIdStatus;


}
