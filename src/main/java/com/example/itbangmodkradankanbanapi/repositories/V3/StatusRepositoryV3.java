package com.example.itbangmodkradankanbanapi.repositories.V3;

import com.example.itbangmodkradankanbanapi.entities.V2.StatusV2;
import com.example.itbangmodkradankanbanapi.entities.V3.StatusV3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepositoryV3 extends JpaRepository<StatusV3,Integer> {
    public StatusV3 findByName(String name);

}
