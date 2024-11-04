package com.example.itbangmodkradankanbanapi.dtos.V3.task;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
@Data
public class TaskAttachmentDto {


    private Integer id;
    private String name;
    private String type;
    private Timestamp addedOn;
}
