package com.example.itbangmodkradankanbanapi.repositories.V3;

import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.StatusV3;
import com.example.itbangmodkradankanbanapi.entities.V3.TaskAttachment;
import com.example.itbangmodkradankanbanapi.entities.V3.TasksV3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment,Integer> {
    public TaskAttachment findByIdAndTask(String fileId, TasksV3 tasks);
}
