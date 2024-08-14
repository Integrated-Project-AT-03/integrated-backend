package com.example.itbangmodkradankanbanapi.repositories.V2;

import com.example.itbangmodkradankanbanapi.entities.V2.UserdataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserdataEntity, String> {
}
