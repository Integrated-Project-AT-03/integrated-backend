package com.example.itbangmodkradankanbanapi.services.V3;

import com.example.itbangmodkradankanbanapi.dtos.V3.board.BoardDtoV3;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoard;
import com.example.itbangmodkradankanbanapi.exceptions.InvalidFieldInputException;
import com.example.itbangmodkradankanbanapi.repositories.V3.ShareBoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.userShare.UserDataRepository;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareBoardService {

    @Autowired
    private ShareBoardRepositoryV3 repository;
    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private ListMapper listMapper;
    public List<BoardDtoV3> getAllBoardHasOwnByUserOid(String oid){
        userDataRepository.findById(oid).orElseThrow(()->  new InvalidFieldInputException("oid","dose not exist !!!!"));
       return   listMapper.mapList( repository.findAllByOidUserShare(oid).stream().map(ShareBoard::getBoard).toList(),BoardDtoV3.class);
    }
}
