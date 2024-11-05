package com.example.itbangmodkradankanbanapi.entities.V3;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "request_collab", schema = "karban")
@IdClass(RequestCollabId.class)
public class RequestCollab {
    @Id
    @Column(name = "oid_user_share")
    private String oidUserShare;
    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ShareBoardsRole role;
    @Id
    @ManyToOne
    @JoinColumn(name = "nano_id_board", referencedColumnName = "nano_id_board", nullable = false)
    private Board board;



    @CreationTimestamp
    @Column(name = "added_on")
    private Timestamp addedOn;

}
