package com.example.itbangmodkradankanbanapi.repositories.V3;

import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.StatusV3;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepositoryV3 extends JpaRepository<StatusV3,Integer> {
    public StatusV3 findByNameAndBoard(String name, Board board);

    public StatusV3 findByName(String name);



}
