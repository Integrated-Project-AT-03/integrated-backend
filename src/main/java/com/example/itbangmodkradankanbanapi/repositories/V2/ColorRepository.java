package com.example.itbangmodkradankanbanapi.repositories.V2;

import com.example.itbangmodkradankanbanapi.entities.V2.ColorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<ColorStatus,Integer> {

}
