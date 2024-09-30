package com.example.itbangmodkradankanbanapi.dtos.V3.board;


import com.example.itbangmodkradankanbanapi.dtos.V3.status.StatusDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.TaskDtoV3;
import lombok.Data;

import java.util.List;

@Data
public class BoardDtoV3 {
    private String id;
    private String name;
}
