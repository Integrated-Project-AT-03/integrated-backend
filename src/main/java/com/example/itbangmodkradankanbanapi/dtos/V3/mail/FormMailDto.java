package com.example.itbangmodkradankanbanapi.dtos.V3.mail;

import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardsRole;
import lombok.Data;

@Data
public class FormMailDto {
    private String oid;
    private String from;
    private String to;
    private ShareBoardsRole role;
    private String boardName;
    private String boardId;
    private String recipientEmail;
}
