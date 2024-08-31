package com.example.itbangmodkradankanbanapi.entities.V3;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "share_board", schema = "karban")
@IdClass(ShareBoardId.class)
public class ShareBoard {
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

}
