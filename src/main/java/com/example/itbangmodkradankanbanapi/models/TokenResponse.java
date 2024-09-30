package com.example.itbangmodkradankanbanapi.models;

import lombok.Data;

import java.util.Map;
@Data
public class TokenResponse {
    private String role;
    private String name;
    private String oid;
    private String email;
    private String sub;
    private String iss;
    private long iat;
    private long exp;
    private String access_token;
    private String refresh_token;

    // Constructor ที่รับข้อมูลจาก claims และ token
    public TokenResponse(Map<String, Object> claims, String accessToken, String refreshToken) {
        this.role = (String) claims.get("role");
        this.name = (String) claims.get("name");
        this.oid = (String) claims.get("oid");
        this.email = (String) claims.get("email");
        this.sub = (String) claims.get("sub");
        this.iss = (String) claims.get("iss");
        this.iat = ((Number) claims.get("iat")).longValue();
        this.exp = ((Number) claims.get("exp")).longValue();
        this.access_token = accessToken;
        this.refresh_token = refreshToken;
    }

    // Getters และ setters
    // (สามารถสร้างอัตโนมัติจาก IDE ของคุณ)
}
