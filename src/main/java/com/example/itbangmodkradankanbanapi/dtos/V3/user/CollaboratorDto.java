package com.example.itbangmodkradankanbanapi.dtos.V3.user;

import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardsRole;
import com.example.itbangmodkradankanbanapi.entities.userShare.UserdataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CollaboratorDto {
    private String oid;
    private String name;
    private String email;
    @JsonIgnore
    private ShareBoardsRole role;
    private String boardNanoId;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private Timestamp addedOn;

    public String getAccessRight(){
       return this.role.equals(ShareBoardsRole.OWNER) ? "owner" : this.role.equals(ShareBoardsRole.WRITER) ? "write" : "read" ;
    }
}


