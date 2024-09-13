package com.example.itbangmodkradankanbanapi.repositories.V3;

import com.example.itbangmodkradankanbanapi.entities.V2.StatusV2;
import com.example.itbangmodkradankanbanapi.entities.V2.TasksV2;
import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.StatusV3;
import com.example.itbangmodkradankanbanapi.entities.V3.TasksV3;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepositoryV3 extends JpaRepository<TasksV3,Integer> {
    List<TasksV3> findByBoardAndStatusIn(Board board,List<StatusV3> statusV3,Sort sort);
    List<TasksV3> findByBoard(Board board);
    List<TasksV3> findByBoard(Board board,Sort sort);
    List<TasksV3> findByStatusIn(List<StatusV3> statusV3, Sort sort);

    List<TasksV3> findByStatusAndBoard(StatusV3 statusV3 , Board board);
}
