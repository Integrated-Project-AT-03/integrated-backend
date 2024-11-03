package com.example.itbangmodkradankanbanapi.dtos.V3.task;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data

public class AddTaskImagesRequest {
    @NotNull
    int taskId;
    @NotNull
    MultipartFile[] files;
}
