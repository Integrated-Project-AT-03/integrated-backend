package com.example.itbangmodkradankanbanapi.dtos.V3.user;

import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardsRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ResultCollaboratorDto {
    @JsonProperty("boardId")
    private String boardNanoId;
    @JsonProperty("CollaboratorName")
    private String name;
    private String collaboratorEmail;
    @JsonIgnore
    private ShareBoardsRole role;


    public String getAccessRight(){
        return this.role.equals(ShareBoardsRole.OWNER) ? "OWNER" : this.role.equals(ShareBoardsRole.WRITER) ? "WRITE" : "READ" ;
    }
}