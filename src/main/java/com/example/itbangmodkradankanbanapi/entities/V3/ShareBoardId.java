package com.example.itbangmodkradankanbanapi.entities.V3;

import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

public class ShareBoardId implements Serializable {
    private String oidUserShare;
    private Board board; // Assuming Board uses its primary key for equality checks

    public ShareBoardId() {
    }

    public ShareBoardId(String oidUserShare, Board board) {
        this.oidUserShare = oidUserShare;
        this.board = board;
    }

    // Getters and Setters

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShareBoardId that = (ShareBoardId) o;
        return Objects.equals(oidUserShare, that.oidUserShare) &&
                Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oidUserShare, board);
    }
}
