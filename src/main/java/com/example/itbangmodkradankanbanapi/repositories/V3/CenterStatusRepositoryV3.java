package com.example.itbangmodkradankanbanapi.repositories.V3;

import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.CenterStatus;
import com.example.itbangmodkradankanbanapi.entities.V3.StatusV3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CenterStatusRepositoryV3 extends JpaRepository<CenterStatus, StatusV3> {
}
