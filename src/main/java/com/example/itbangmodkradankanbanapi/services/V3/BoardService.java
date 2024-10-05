package com.example.itbangmodkradankanbanapi.services.V3;

import com.example.itbangmodkradankanbanapi.Auth.JwtTokenUtil;
import com.example.itbangmodkradankanbanapi.dtos.V3.board.*;
import com.example.itbangmodkradankanbanapi.dtos.V3.status.StatusDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.task.TaskDtoV3;
import com.example.itbangmodkradankanbanapi.entities.V3.*;
import com.example.itbangmodkradankanbanapi.entities.userShare.UserdataEntity;
import com.example.itbangmodkradankanbanapi.exceptions.InvalidFieldInputException;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.exceptions.NotAllowedException;
import com.example.itbangmodkradankanbanapi.repositories.V3.*;
import com.example.itbangmodkradankanbanapi.repositories.userShare.UserDataRepository;
import com.example.itbangmodkradankanbanapi.utils.CustomNanoId;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BoardService {
    @Autowired
    private ShareBoardRepositoryV3 shareBoardRepository;

    @Autowired
    private TaskRepositoryV3 taskRepositoryV3;

    @Autowired
    private StatusRepositoryV3 statusRepositoryV3;

    @Autowired
    private BoardRepositoryV3 repository;
    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private CenterStatusRepositoryV3 centerStatusRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper = new ModelMapper();

    public FormBoardSettingDtoV3 updateBoardSettings(String nanoId,FormBoardSettingDtoV3 settingForm){
        Board board = repository.findById(nanoId).orElseThrow(() -> new NoSuchElementException("Board id "+ nanoId + " not found"));
        board.setEnableLimitsTask(settingForm.getEnableLimitsTask());
        board.setLimitsTask(settingForm.getLimitsTask());
        return modelMapper.map(repository.save(board),FormBoardSettingDtoV3.class);
    }

    public BoardSettingDtoV3 getBoardSettings(String nanoId){
        Board board = repository.findById(nanoId).orElseThrow(() -> new NoSuchElementException("Board id "+ nanoId + " not found"));
        return modelMapper.map(repository.save(board),BoardSettingDtoV3.class);
    }

    public FullBoardDtoV3 getBoard(String nanoId,HttpServletRequest request){
        Board board = repository.findById(nanoId).orElseThrow(() -> new NoSuchElementException("Board id "+ nanoId + " not found"));
        FullBoardDtoV3 boardDto = modelMapper.map(board,FullBoardDtoV3.class);
        String oidOwner = board.getShareBoards().stream().filter(shareBoard -> shareBoard.getRole() == ShareBoardsRole.OWNER).findFirst().orElseThrow(()-> new NotAllowedException("The default board is not allowed to access")).getOidUserShare();
        UserdataEntity user = userDataRepository.findById(oidOwner).orElseThrow(()-> new ItemNotFoundException("Not found user oid "+ oidOwner ));

        FullBoardDtoV3.Owner owner = new FullBoardDtoV3.Owner();
        owner.setOid(user.getOid());
        owner.setUsername(user.getName());
        boardDto.setOwner(owner);
        String token = jwtTokenUtil.getTokenCookie(request.getCookies());
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        ShareBoard shareBoard = shareBoardRepository.findByOidUserShareAndBoard(claims.get("oid").toString(),board);
        if(token == null || shareBoard == null) boardDto.setAccess("GUEST");
        else {
            boardDto.setAccess(shareBoard.getRole().toString());
        }

        return boardDto;

    }

    public FormBoardVisibilityDtoV3 updateVisibilityBoard(String nanoId, FormBoardVisibilityDtoV3 formBoard){
        Board board = repository.findById(nanoId).orElseThrow(() -> new NoSuchElementException("Board id "+ nanoId + " not found"));
        board.setIsPublic(formBoard.getIsPublic());
        Board updateBoard = repository.save(board);
        FormBoardVisibilityDtoV3 boardDto = modelMapper.map(updateBoard,FormBoardVisibilityDtoV3.class);
        return boardDto;
    }

    @Transactional
    public BoardDtoV3 createBoard(FormBoardDtoV3 newBoardForm){
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
        BoardDtoV3 boardDto = modelMapper.map(board,BoardDtoV3.class);
        owner.setOid(user.getOid());
        owner.setUsername(user.getName());
        return boardDto;
    }

    public List<BoardDtoV3> getAllBoard(HttpServletRequest request){
      String token = jwtTokenUtil.getTokenCookie(request.getCookies());
      Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        return   listMapper.mapList( shareBoardRepository.findAllByOidUserShareAndRole(claims.get("oid").toString(),ShareBoardsRole.OWNER).stream().map(ShareBoard::getBoard).toList(),BoardDtoV3.class);
    }

    public List<TaskDtoV3> getAllTasksByBoardAndFilter(String nanoId,String[] filterStatuses, String[] sortBy,String[] direction) {
      Board board =  repository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Board id "+ nanoId + " not found"));
            List<Sort.Order> orders = new ArrayList<>();

        if((sortBy.length != 0 && !sortBy[0].equals("status.name"))|| sortBy.length > 1 ) throw new NotAllowedException("invalid filter parameter");

        else if(sortBy.length !=0) {
            for (int i = 0; i < sortBy.length; i++) {
                orders.add(new Sort.Order((direction[i].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC), sortBy[i]));
            }
    }
        orders.add(new Sort.Order(Sort.Direction.ASC ,"createdOn"));
        if(filterStatuses.length == 0) return  listMapper.mapList(taskRepositoryV3.findAllByBoard(board,Sort.by(orders)),TaskDtoV3.class);

        List<StatusV3> statuses = Arrays.stream(filterStatuses).map((filterStatus) -> statusRepositoryV3.findByName(filterStatus.replace("_"," "))).toList();
        return  listMapper.mapList(taskRepositoryV3.findAllByBoardAndStatusIn(board,statuses,Sort.by(orders)),TaskDtoV3.class);
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
