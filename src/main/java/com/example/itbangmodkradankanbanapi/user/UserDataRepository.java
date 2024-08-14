package com.example.itbangmodkradankanbanapi.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDataRepository extends JpaRepository<UserdataEntity, String> {
    UserdataEntity findByUsername(String name);
}
