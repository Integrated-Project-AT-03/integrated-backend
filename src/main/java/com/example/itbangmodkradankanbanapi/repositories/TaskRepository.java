package com.example.itbangmodkradankanbanapi.repositories;

import com.example.itbangmodkradankanbanapi.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,String> {
}
