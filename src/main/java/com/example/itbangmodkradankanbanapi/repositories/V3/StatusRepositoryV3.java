package com.example.itbangmodkradankanbanapi.repositories.V3;

import com.example.itbangmodkradankanbanapi.entities.V2.StatusV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepositoryV3 extends JpaRepository<StatusV2,Integer> {
    public StatusV2 findByName(String name);

}
