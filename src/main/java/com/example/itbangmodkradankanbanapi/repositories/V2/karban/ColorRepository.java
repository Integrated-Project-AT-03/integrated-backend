package com.example.itbangmodkradankanbanapi.repositories.V2.karban;

import com.example.itbangmodkradankanbanapi.entities.V2.karban.ColorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<ColorStatus,Integer> {

}
