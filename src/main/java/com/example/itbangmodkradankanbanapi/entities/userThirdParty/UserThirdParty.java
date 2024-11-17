package com.example.itbangmodkradankanbanapi.entities.userThirdParty;

import com.example.itbangmodkradankanbanapi.models.UserThirdPartyPlatform;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "user_third_party", schema = "karban")
public class UserThirdParty {

    @Id
    private String oid;
    private String name;
    private String email;
    @Basic
    @Enumerated(EnumType.STRING)
    private UserThirdPartyPlatform platform;
    @CreationTimestamp
    private Timestamp register_on;

}
