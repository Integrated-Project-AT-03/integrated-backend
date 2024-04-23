package com.example.itbangmodkradankanbanapi.entities;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class Employee {
    @Column(name = "empId")
    @Id
    private Integer empId;
    @Basic
    @Column(name = "empName")
    private String empName;

    @Column(name = "createTime", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createTime;

    @Column(name = "updateTime")
    @UpdateTimestamp
    private Timestamp updateTime;

}
