package com.example.itbangmodkradankanbanapi.repositories.V1;

import com.example.itbangmodkradankanbanapi.entities.V1.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Integer> {
    List<Task> findAllByOrderByCreatedOnAsc();
}
