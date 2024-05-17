package com.example.itbangmodkradankanbanapi.repositories.V1;

import com.example.itbangmodkradankanbanapi.entities.V1.Task;
import com.example.itbangmodkradankanbanapi.entities.V2.TasksV2;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Integer> {

    List<Task> findAllByOrderByCreatedOnAsc(Sort sort);
    List<Task> findAllByOrderByCreatedOnAsc();
}
