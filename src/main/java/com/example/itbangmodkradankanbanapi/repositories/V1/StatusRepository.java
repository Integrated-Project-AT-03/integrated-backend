package com.example.itbangmodkradankanbanapi.repositories.V1;

import com.example.itbangmodkradankanbanapi.entities.V1.Status;
import com.example.itbangmodkradankanbanapi.entities.V1.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status,Integer> {
    public Status findByStatusType(StatusType statusType);
}
