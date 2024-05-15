package com.example.itbangmodkradankanbanapi.entities.V2;

import com.example.itbangmodkradankanbanapi.entities.V2.StatusV2;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "color_status", schema = "karban")
public class ColorStatus {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_color")
    private Integer id;
    @Basic
    @Column(name = "color_name")
    private String name;
    @Basic
    @Column(name = "color_hex")
    private String hex;
    @OneToMany(mappedBy = "color")
    private List<StatusV2> statuses;
}
