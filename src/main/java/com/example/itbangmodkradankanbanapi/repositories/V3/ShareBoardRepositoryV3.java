package com.example.itbangmodkradankanbanapi.repositories.V3;
import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoard;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShareBoardRepositoryV3 extends JpaRepository<ShareBoard, ShareBoardId> {
    List<ShareBoard> findAllByOidUserShare(String oid);

    List<ShareBoard> findAllByBoard(Board board);

    ShareBoard findByOidUserShareAndBoard(String oid, Board board);
}