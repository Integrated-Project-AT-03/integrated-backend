package com.example.itbangmodkradankanbanapi.repositories;

import com.example.itbangmodkradankanbanapi.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,String> {
    List<Task> findAllByOrderByCreatedOnAsc();
}
