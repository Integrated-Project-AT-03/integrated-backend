package com.example.itbangmodkradankanbanapi.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserdataEntity, String> {
}
