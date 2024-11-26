package com.example.itbangmodkradankanbanapi.repositories.V3;

import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.StatusV3;
import com.example.itbangmodkradankanbanapi.entities.V3.TasksV3;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepositoryV3 extends JpaRepository<TasksV3,Integer> {
    List<TasksV3> findAllByBoardAndStatusIn(Board board, List<StatusV3> statusV3, Sort sort);
    List<TasksV3> findAllByBoard(Board board, Sort sort);
    List<TasksV3> findAllByStatusIn(List<StatusV3> statusV3, Sort sort);

    int countByStatusAndAndBoard(StatusV3 status , Board board);
    List<TasksV3> findAllByStatusAndBoard(StatusV3 statusV3 , Board board);
}
