package com.example.itbangmodkradankanbanapi.repositories.V3;
import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoard;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardId;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardsRole;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShareBoardRepositoryV3 extends JpaRepository<ShareBoard, ShareBoardId> {
    List<ShareBoard> findAllByOidUserShare(String oid);

    List<ShareBoard> findAllByOidUserShareAndRole(String oid, ShareBoardsRole role);

    List<ShareBoard> findAllByOidUserShareAndRoleNot(String oid, ShareBoardsRole role, Sort sort);


    List<ShareBoard> findAllByBoard(Board board);

    List<ShareBoard> findAllByBoardAndRoleNot(Board board,ShareBoardsRole shareBoardsRole, Sort sort);

    ShareBoard findByBoardAndOidUserShareAndRoleNot(Board board,String oid,ShareBoardsRole shareBoardsRole);

    ShareBoard findByBoardAndRole(Board board, ShareBoardsRole role);


    ShareBoard findByOidUserShareAndBoard(String oid, Board board);
}