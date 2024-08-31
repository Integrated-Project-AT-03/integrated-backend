package com.example.itbangmodkradankanbanapi.repositories.V2;

import com.example.itbangmodkradankanbanapi.entities.V2.StatusV2;
import com.example.itbangmodkradankanbanapi.entities.V2.TasksV2;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepositoryV2 extends JpaRepository<TasksV2,Integer> {
    List<TasksV2> findByStatusIn(List<StatusV2> statusV2, Sort sort);
}
