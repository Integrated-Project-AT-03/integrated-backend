package com.example.itbangmodkradankanbanapi.repositories.V3;

import com.example.itbangmodkradankanbanapi.entities.V3.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestCollabRepositoryV3 extends JpaRepository<RequestCollab, RequestCollabId> {

    List<RequestCollab> findAllByBoard(Board board);
//    List<ShareBoard> findAllByOidUserShare(String oid);
//
//    List<ShareBoard> findAllByOidUserShareAndRole(String oid, ShareBoardsRole role);
//
//    List<ShareBoard> findAllByOidUserShareAndRoleNot(String oid, ShareBoardsRole role, Sort sort);
//
//
//    List<ShareBoard> findAllByBoard(Board board);
//
//    List<ShareBoard> findAllByBoardAndRoleNot(Board board,ShareBoardsRole shareBoardsRole, Sort sort);
//
//    ShareBoard findByBoardAndOidUserShareAndRoleNot(Board board,String oid,ShareBoardsRole shareBoardsRole);
//
//    ShareBoard findByBoardAndRole(Board board, ShareBoardsRole role);
//
//
//    ShareBoard findByOidUserShareAndBoard(String oid, Board board);
}