package com.example.itbangmodkradankanbanapi.repositories.V3;

import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoard;
import com.example.itbangmodkradankanbanapi.entities.V3.StatusV3;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepositoryV3 extends JpaRepository<Board,String> {
}
