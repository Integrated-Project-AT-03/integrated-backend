package com.example.itbangmodkradankanbanapi.dtos.V3.board;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class FullBoardDtoV3 {
    private String id;
    private String name;
    private String visibility;
    private Owner owner = null;

    private String access = null;
    @JsonIgnore
    private Boolean isPublic;


    @Data
    public static class Owner {
        private String oid;
        private String username;
    }


    public String getVisibility() {
        return isPublic ? "PUBLIC" : "PRIVATE";
    }

}
