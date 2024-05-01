package com.example.itbangmodkradankanbanapi.repositories;

import com.example.itbangmodkradankanbanapi.entities.Status;
import com.example.itbangmodkradankanbanapi.entities.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status,Integer> {
    public Status findByStatusType(StatusType statusType);
}
