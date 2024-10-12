package com.example.itbangmodkradankanbanapi.services.V3;

import com.example.itbangmodkradankanbanapi.Auth.JwtTokenUtil;
import com.example.itbangmodkradankanbanapi.dtos.V3.collaborator.*;
import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoard;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardId;
import com.example.itbangmodkradankanbanapi.entities.V3.ShareBoardsRole;
import com.example.itbangmodkradankanbanapi.entities.userShare.UserdataEntity;
import com.example.itbangmodkradankanbanapi.exceptions.*;
import com.example.itbangmodkradankanbanapi.repositories.V3.BoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.ShareBoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.userShare.UserDataRepository;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CollaboratorService {

    @Autowired
    private ShareBoardRepositoryV3 repository;
    @Autowired
    private UserDataRepository userDataRepository;
    @Autowired
    private BoardRepositoryV3 boardRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;




    @Autowired
    private ListMapper listMapper;
    public List<CollaboratorDto> getAllCollaboratorByNanoId(String nanoId){
        Board board = boardRepository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Not Found Boards"));
       List<ShareBoard> shareBoards = repository.findAllByBoardAndRoleNot(board,ShareBoardsRole.OWNER);

        return listMapper.mapList(shareBoards,CollaboratorDto.class).stream().peek((shareBoard)-> {
           UserdataEntity userdata = userDataRepository.findById(shareBoard.getOid()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));

           shareBoard.setName(userdata.getName());
           shareBoard.setEmail(userdata.getEmail());
       }).toList();
    }
    @Transactional
    public Map<String,String> updateAccessBoard(String nanoId,String oid, UpdateAccessCollaboratorDto form){
        Board board = boardRepository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Not Found Boards"));
        ShareBoard shareBoard = repository.findById(new ShareBoardId(oid,board)).orElseThrow(() -> new NoSuchElementException("not found this collaborator"));
        if(!form.getAccessRight().equals("WRITE") && !form.getAccessRight().equals("READ")  )  throw new NotAllowedException("Only Add Read or Write");
        ShareBoardsRole role = form.getAccessRight().equals("WRITE") ? ShareBoardsRole.WRITER : ShareBoardsRole.READER;
        shareBoard.setRole(role);
        repository.save(shareBoard);
        Map<String,String> result = new HashMap<>();
        result.put("accessRight",form.getAccessRight());
        return result;
    }
    @Transactional
    public void removeCollaborator(HttpServletRequest request,String nanoId,String oid){
        Board board = boardRepository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Not Found Board"));
        String token = jwtTokenUtil.getTokenCookie(request.getCookies());
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        ShareBoard shareBoardByToken = repository.findById(new ShareBoardId(claims.get("oid").toString(),board)).orElseThrow(()-> new ItemNotFoundException("Not Found this user in board by Token"));
        ShareBoard shareBoard = repository.findById(new ShareBoardId(oid,board)).orElseThrow(()-> new ItemNotFoundException("Not Found this user in this board"));
        if(!shareBoardByToken.getRole().equals(ShareBoardsRole.OWNER) && !shareBoard.getOidUserShare().equals(claims.get("oid").toString())) throw new NoAccessException("No access to delete role another if you are not the owner!!");
        if(shareBoardByToken.getRole().equals(ShareBoardsRole.OWNER) && shareBoard.getOidUserShare().equals(claims.get("oid").toString())) throw new ConflictException("You are the owner can't remove yourself!!");
        repository.delete(shareBoard);
    }


    public List<CollaboratorBoardDto> getAllCollaborator(HttpServletRequest request){
        String token = jwtTokenUtil.getTokenCookie(request.getCookies());
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        String oid = claims.get("oid").toString();
        List<ShareBoard> shareBoards = repository.findAllByOidUserShareAndRoleNot(oid,ShareBoardsRole.OWNER);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        return listMapper.mapList(shareBoards,CollaboratorBoardDto.class).stream().peek((shareBoard)-> {
            ShareBoard shareBoardOwner = repository.findByBoardAndRole(shareBoards.get(  atomicInteger.getAndIncrement()).getBoard(),ShareBoardsRole.OWNER);
            UserdataEntity userdata = userDataRepository.findById(shareBoardOwner.getOidUserShare()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
            shareBoard.setName(userdata.getName());
            shareBoard.setEmail(userdata.getEmail());
        }).toList();
    }

    @Transactional
    public CollaboratorDto addCollaborator(HttpServletRequest request, String nanoId, FormCollaboratorDto form){
      Board board = boardRepository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Not Found Boards"));
       String token = jwtTokenUtil.getTokenCookie(request.getCookies());
       Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
       ShareBoard shareBoard = repository.findById(new ShareBoardId(claims.get("oid").toString(),board)).orElseThrow(()-> new ItemNotFoundException("Not Found this user in board by Token"));
        UserdataEntity userdata = userDataRepository.findByEmail(form.getEmail());
        if(userdata == null) throw new ItemNotFoundException("Not Found User");
        ShareBoard shareBoardCollab = repository.findById(new ShareBoardId(userdata.getOid(),board)).orElse(null);
        if(shareBoardCollab != null && shareBoardCollab.getRole().equals(ShareBoardsRole.OWNER)) throw new ConflictException("Board owner cannot be collaborator of his/her own board.");
        if(repository.findById(new ShareBoardId(userdata.getOid(),board)).orElse(null) != null) throw new ConflictException("The user is already the collaborator of this board.");
        if(!shareBoard.getRole().equals(ShareBoardsRole.OWNER)) throw new NoAccessException("The owner only is allow for this action!!");




        ShareBoard newShareBoard = new ShareBoard();
        ShareBoardsRole role = form.getAccessRight().equals("WRITE") ? ShareBoardsRole.WRITER : ShareBoardsRole.READER;
        newShareBoard.setRole(role);
        newShareBoard.setBoard(board);
        newShareBoard.setOidUserShare(userdata.getOid());
        CollaboratorDto result =  mapper.map(repository.save(newShareBoard),CollaboratorDto.class);
        result.setName(userdata.getName());
        result.setEmail(userdata.getEmail());
        return  result;
    }
}
