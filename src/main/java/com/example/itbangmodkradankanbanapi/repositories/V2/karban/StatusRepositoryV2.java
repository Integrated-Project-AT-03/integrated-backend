package com.example.itbangmodkradankanbanapi.repositories.V2.karban;

import com.example.itbangmodkradankanbanapi.entities.V2.karban.StatusV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepositoryV2 extends JpaRepository<StatusV2,Integer> {
    public StatusV2 findByName(String name);

}
