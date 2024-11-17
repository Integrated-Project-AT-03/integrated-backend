package com.example.itbangmodkradankanbanapi.repositories.user;

import com.example.itbangmodkradankanbanapi.entities.user.UserdataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataCenterRepository extends JpaRepository<UserdataEntity, String> {
    UserdataEntity findByUsername(String name);
    UserdataEntity findByEmail(String email);
}
