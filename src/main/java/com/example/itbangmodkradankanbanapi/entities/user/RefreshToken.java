package com.example.itbangmodkradankanbanapi.entities.user;

import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "refreshtoken")
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private Instant expiryDate;

}
