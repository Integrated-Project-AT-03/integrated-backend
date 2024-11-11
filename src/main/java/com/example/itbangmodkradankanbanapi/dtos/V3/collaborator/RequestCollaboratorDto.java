package com.example.itbangmodkradankanbanapi.dtos.V3.collaborator;

import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardsRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class RequestCollaboratorDto {
    private String oid;
    private String name;
    private String email;
    @JsonIgnore
    private ShareBoardsRole role;
    private String boardNanoId;
    private String boardName;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private Timestamp addedOn;

    @Schema(allowableValues = {"PADDING", "CANCEL"})
    private String status ;

    private Boolean isSendEmail;

    public String getAccessRight(){
       return this.role.equals(ShareBoardsRole.OWNER) ? "OWNER" : this.role.equals(ShareBoardsRole.WRITER) ? "WRITE" : "READ" ;
    }

}


