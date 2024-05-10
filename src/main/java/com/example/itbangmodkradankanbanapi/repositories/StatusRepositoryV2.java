package com.example.itbangmodkradankanbanapi.repositories;

import com.example.itbangmodkradankanbanapi.entities.Status;
import com.example.itbangmodkradankanbanapi.entities.StatusType;
import com.example.itbangmodkradankanbanapi.entities.StatusV2;
import com.example.itbangmodkradankanbanapi.entities.TasksV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepositoryV2 extends JpaRepository<StatusV2,Integer> {
    public StatusV2 findByStatusName(String statusName);
}
