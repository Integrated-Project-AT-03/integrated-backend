package com.example.itbangmodkradankanbanapi.entities.V3;

import com.example.itbangmodkradankanbanapi.utils.CustomNanoId;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "boards")
public class Board {
    @Id
    @Column(name = "nano_id_board")
    private String nanoIdBoard ;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "enable_limits_task")
    private Boolean enableLimitsTask = true;

    @Basic
    @Column(name = "public")
    private Boolean isPublic = false;

    @Basic
    @Column(name = "limits_task")
    private Integer limitsTask = 10;
    @Basic
    @Column(name = "enable_status_center")
    private Integer enableStatusCenter = 15;

    @OneToMany(mappedBy = "board")
    private List<ShareBoard> shareBoards = new ArrayList<>();;
    @OneToMany(mappedBy = "board")
    private List<StatusV3> statuses = new ArrayList<>();;
    @OneToMany(mappedBy = "board")
    private List<TasksV3> tasks = new ArrayList<>();;

    public String getEnableStatusCenter() {
        if (enableStatusCenter != null) {
            // Format the integer as a 4-bit binary string
            return String.format("%4s", Integer.toBinaryString(enableStatusCenter))
                    .replace(' ', '0'); // Pad with leading zeros if necessary
        }
        return null;
    }

    public void setEnableStatusCenter(String enableStatusCenter) {
        if (enableStatusCenter != null) {
            this.enableStatusCenter = Integer.parseInt(enableStatusCenter, 2);
        } else {
            this.enableStatusCenter = null;
        }
    }

}
