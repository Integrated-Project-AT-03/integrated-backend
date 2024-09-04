package com.example.itbangmodkradankanbanapi.services.V3;

import com.example.itbangmodkradankanbanapi.dtos.V1.FormTaskDto;
import com.example.itbangmodkradankanbanapi.dtos.V1.FullTaskDto;
import com.example.itbangmodkradankanbanapi.dtos.V1.TaskDto;
import com.example.itbangmodkradankanbanapi.dtos.V2.TaskDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V3.board.BoardDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.board.FormBoardDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.board.FormBoardSettingDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.board.FullBoardDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.status.StatusDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.FormTaskDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.TaskDtoV3;
import com.example.itbangmodkradankanbanapi.entities.V1.Task;
import com.example.itbangmodkradankanbanapi.entities.V2.Setting;
import com.example.itbangmodkradankanbanapi.entities.V3.*;
import com.example.itbangmodkradankanbanapi.entities.userShare.UserdataEntity;
import com.example.itbangmodkradankanbanapi.exceptions.InvalidFieldInputException;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.exceptions.NotAllowedException;
import com.example.itbangmodkradankanbanapi.models.SettingLockStatus;
import com.example.itbangmodkradankanbanapi.repositories.V1.StatusRepository;
import com.example.itbangmodkradankanbanapi.repositories.V1.TaskRepository;
import com.example.itbangmodkradankanbanapi.repositories.V3.BoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.CenterStatusRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.ShareBoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.userShare.UserDataRepository;
import com.example.itbangmodkradankanbanapi.utils.CustomNanoId;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BoardService {
    @Autowired
    private ShareBoardRepositoryV3 shareBoardRepository;

    @Autowired
    private BoardRepositoryV3 repository;
    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private CenterStatusRepositoryV3 centerStatusRepository;

    @Autowired
    private ListMapper listMapper;
    private final ModelMapper modelMapper = new ModelMapper();

    public FormBoardSettingDtoV3 updateBoardSettings(String nanoId,FormBoardSettingDtoV3 settingForm){
        Board board = repository.findById(nanoId).orElseThrow(() -> new NoSuchElementException("Board id "+ nanoId + " not found"));
        board.setEnableLimitsTask(settingForm.getEnableLimitsTask());
        board.setLimitsTask(settingForm.getLimitsTask());
        return modelMapper.map(repository.save(board),FormBoardSettingDtoV3.class);
    }

    public FormBoardSettingDtoV3 getBoardSettings(String nanoId){
        Board board = repository.findById(nanoId).orElseThrow(() -> new NoSuchElementException("Board id "+ nanoId + " not found"));
        return modelMapper.map(repository.save(board),FormBoardSettingDtoV3.class);
    }

    public FullBoardDtoV3 getBoard(String nanoId){
        Board board = repository.findById(nanoId).orElseThrow(() -> new NoSuchElementException("Board id "+ nanoId + " not found"));
        FullBoardDtoV3 boardDto = modelMapper.map(board,FullBoardDtoV3.class);
        String oidOwner = board.getShareBoards().stream().filter(shareBoard -> shareBoard.getRole() == ShareBoardsRole.OWNER).findFirst().orElseThrow(()-> new NotAllowedException("The default board is not allowed to access")).getOidUserShare();
        UserdataEntity user = userDataRepository.findById(oidOwner).orElseThrow(()-> new ItemNotFoundException("Not found user oid "+ oidOwner ));
        FullBoardDtoV3.Owner owner = new FullBoardDtoV3.Owner();
        owner.setOid(user.getOid());
        owner.setName(user.getName());
        boardDto.setOwner(owner);
        return boardDto;

    }

    @Transactional
    public FullBoardDtoV3 createBoard(FormBoardDtoV3 newBoardForm){
      UserdataEntity user = userDataRepository.findById(newBoardForm.getOwnerOid()).orElseThrow(()-> new InvalidFieldInputException("owner","not found user id " + newBoardForm.getOwnerOid()));
        Board newBoard = new Board();
        String nanoId = CustomNanoId.generate(10);
        AtomicInteger time = new AtomicInteger(0);
        while (repository.findById(nanoId).orElse(null) != null) {
            nanoId = CustomNanoId.generate(10);
           if (time.getAndIncrement() == 10) throw new NotAllowedException("Boards is too much for creating");
        }
        newBoard.setNanoIdBoard(nanoId);
        newBoard.setName(newBoardForm.getName());
        Board board =  repository.save(newBoard);
        ShareBoard newShareBoard = new ShareBoard();
        newShareBoard.setBoard(board);
        newShareBoard.setRole(ShareBoardsRole.OWNER);
        newShareBoard.setOidUserShare(newBoardForm.getOwnerOid());
        shareBoardRepository.save(newShareBoard);
        FullBoardDtoV3.Owner owner = new FullBoardDtoV3.Owner();
        FullBoardDtoV3 boardDto = modelMapper.map(board,FullBoardDtoV3.class);
        owner.setOid(user.getOid());
        owner.setName(user.getName());
        boardDto.setOwner(owner);
        return boardDto;
    }

    public List<BoardDtoV3> getAllBoard(){
        return listMapper.mapList(repository.findAll(), BoardDtoV3.class);
    }

    public List<TaskDtoV2> getAllTasksByBoard(String nanoId){
        return listMapper.mapList(repository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Board id "+ nanoId + " not found")).getTasks(), TaskDtoV2.class);
    }


    public List<StatusDtoV3> getAllStatusesByBoard(String nanoId){

        Board board = repository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Board id "+ nanoId + " not found"));
        List<StatusV3> tempStatus = new ArrayList<>();

        AtomicInteger index = new AtomicInteger(0);
        List<StatusV3> centerStatus = centerStatusRepository.findAll().stream().map(CenterStatus::getStatus).filter((status) -> {
            int idx = index.getAndIncrement();
           return String.valueOf(board.getEnableStatusCenter().charAt(idx)).equals("1");
        }).toList();
        tempStatus.addAll(centerStatus);
        tempStatus.addAll( board.getStatuses());

        return listMapper.mapList(tempStatus, StatusDtoV3.class);
    }
}
