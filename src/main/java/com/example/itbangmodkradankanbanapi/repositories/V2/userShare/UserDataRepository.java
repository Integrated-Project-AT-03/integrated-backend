package com.example.itbangmodkradankanbanapi.repositories.V2.userShare;

import com.example.itbangmodkradankanbanapi.entities.userShare.UserdataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserdataEntity, String> {
    UserdataEntity findByUsername(String name);
}
