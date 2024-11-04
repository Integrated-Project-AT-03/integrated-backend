package com.example.itbangmodkradankanbanapi.services.V3;

import com.example.itbangmodkradankanbanapi.Auth.JwtTokenUtil;
import com.example.itbangmodkradankanbanapi.dtos.V3.collaborator.*;
import com.example.itbangmodkradankanbanapi.entities.V3.*;
import com.example.itbangmodkradankanbanapi.entities.userShare.UserdataEntity;
import com.example.itbangmodkradankanbanapi.exceptions.*;
import com.example.itbangmodkradankanbanapi.repositories.V3.BoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.RequestCollabRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.ShareBoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.userShare.UserDataRepository;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CollaboratorService {

    @Autowired
    private ShareBoardRepositoryV3 repository;
    @Autowired
    private RequestCollabRepositoryV3 requestCollabRepository;
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
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC ,"addedOn"));
        List<ShareBoard> shareBoards = repository.findAllByBoardAndRoleNot(board,ShareBoardsRole.OWNER,Sort.by(orders));
        List<RequestCollab> requestCollabs  = requestCollabRepository.findAllByBoard(board);
        List<CollaboratorDto> tempCollaboratorsDto = new ArrayList<>();

        tempCollaboratorsDto.addAll(listMapper.mapList(shareBoards,CollaboratorDto.class).stream().peek((collab)-> {
           UserdataEntity userdata = userDataRepository.findById(collab.getOid()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
            collab.setName(userdata.getName());
            collab.setEmail(userdata.getEmail());
            collab.setStatus("ACTIVE");
       }).toList());

        tempCollaboratorsDto.addAll(listMapper.mapList(requestCollabs,CollaboratorDto.class).stream().peek((requestCollab)-> {
            UserdataEntity userdata = userDataRepository.findById(requestCollab.getOid()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
            requestCollab.setName(userdata.getName());
            requestCollab.setEmail(userdata.getEmail());
            requestCollab.setStatus("PADDING");
        }).toList());

        return  tempCollaboratorsDto;
    }

    public CollaboratorDto getCollaboratorByNanoIdAndOid(String nanoId,String oid){
        Board board = boardRepository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Not Found Boards"));
        ShareBoard shareBoard = repository.findByBoardAndOidUserShareAndRoleNot(board,oid,ShareBoardsRole.OWNER);
        if(shareBoard == null) throw  new ItemNotFoundException("Not found this collaborator");
        CollaboratorDto collaborator = mapper.map(shareBoard,CollaboratorDto.class);
        UserdataEntity userdata = userDataRepository.findById(collaborator.getOid()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
        collaborator.setName(userdata.getName());
        collaborator.setEmail(userdata.getEmail());
        return  collaborator;
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
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC ,"addedOn"));
        List<ShareBoard> shareBoards = repository.findAllByOidUserShareAndRoleNot(oid,ShareBoardsRole.OWNER,Sort.by(orders));
        AtomicInteger atomicInteger = new AtomicInteger(0);
        return listMapper.mapList(shareBoards,CollaboratorBoardDto.class).stream().peek((shareBoard)-> {
            ShareBoard shareBoardOwner = repository.findByBoardAndRole(shareBoards.get(  atomicInteger.getAndIncrement()).getBoard(),ShareBoardsRole.OWNER);
            UserdataEntity userdata = userDataRepository.findById(shareBoardOwner.getOidUserShare()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
            shareBoard.setName(userdata.getName());
            shareBoard.setEmail(userdata.getEmail());
        }).toList();
    }


//    public List<InviteBoardDto> getAllColllllaborator(HttpServletRequest request){
//        String token = jwtTokenUtil.getTokenCookie(request.getCookies());
//        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
//        String oid = claims.get("oid").toString();
//        List<Sort.Order> orders = new ArrayList<>();
//        orders.add(new Sort.Order(Sort.Direction.ASC ,"addedOn"));
//        List<ShareBoard> shareBoards = requestCollabRepository.findAllByOidUserShareAndRoleNot(oid,ShareBoardsRole.OWNER,Sort.by(orders));
//        AtomicInteger atomicInteger = new AtomicInteger(0);
//        return listMapper.mapList(shareBoards,CollaboratorBoardDto.class).stream().peek((shareBoard)-> {
//            ShareBoard shareBoardOwner = repository.findByBoardAndRole(shareBoards.get(  atomicInteger.getAndIncrement()).getBoard(),ShareBoardsRole.OWNER);
//            UserdataEntity userdata = userDataRepository.findById(shareBoardOwner.getOidUserShare()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
//            shareBoard.setName(userdata.getName());
//            shareBoard.setEmail(userdata.getEmail());
//        }).toList();
//    }

    @Transactional
    public CollaboratorDto receiveCollaborator(HttpServletRequest request,FormReceiveCollaboratorDto form){
      Board board = boardRepository.findById(form.getBoardNanoId()).orElseThrow(()-> new ItemNotFoundException("Not Found Boards: "+ form.getBoardNanoId()));
       String token = jwtTokenUtil.getTokenCookie(request.getCookies());
       String oid = jwtTokenUtil.getAllClaimsFromToken(token).get("oid").toString();
       RequestCollab requestCollab =  requestCollabRepository.findById(new RequestCollabId(oid,board)).orElseThrow(()-> new ItemNotFoundException("You not invited for this board"));
        UserdataEntity userdata = userDataRepository.findById(oid).orElseThrow(()-> new ItemNotFoundException("Not Found User"));

        ShareBoard newShareBoard = new ShareBoard();
        newShareBoard.setRole(requestCollab.getRole());
        newShareBoard.setBoard(board);
        newShareBoard.setOidUserShare(requestCollab.getOidUserShare());
        newShareBoard.setAddedOn(requestCollab.getAddedOn());
        CollaboratorDto result;
        if(form.getReceiveInvite().equals("ACCEPT")) {
            result = mapper.map(repository.save(newShareBoard), CollaboratorDto.class);
            result.setStatus("ACCEPT");
        }
        else {
            result = mapper.map(newShareBoard, CollaboratorDto.class);
            result.setStatus("DECLINE");
        }
        requestCollabRepository.delete(requestCollab);
        result.setName(userdata.getName());
        result.setEmail(userdata.getEmail());
        result.setStatus(form.getReceiveInvite());
        return  result;
    }


    @Transactional
    public RequestCollaboratorDto cancelInviteCollaborator(String nanoId,String oid){
        Board board = boardRepository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Not Found Boards: "+ nanoId));
        UserdataEntity userdata = userDataRepository.findById(oid).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
        RequestCollab requestCollab =  requestCollabRepository.findById(new RequestCollabId(oid,board)).orElseThrow(()-> new ItemNotFoundException(" Not found invite for this board"));
        RequestCollaboratorDto result = mapper.map(requestCollab, RequestCollaboratorDto.class);
        requestCollabRepository.delete(requestCollab);
        result.setName(userdata.getName());
        result.setEmail(userdata.getEmail());
        result.setStatus("CANCEL");
        return  result;
    }



    @Transactional
    public RequestCollaboratorDto inviteCollaborator(HttpServletRequest request, String nanoId,FormCollaboratorDto form){
        Board board = boardRepository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Not Found Boards"));
        String token = jwtTokenUtil.getTokenCookie(request.getCookies());
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        ShareBoard shareBoard = repository.findById(new ShareBoardId(claims.get("oid").toString(),board)).orElseThrow(()-> new ItemNotFoundException("Not Found this user in board by Token"));
        UserdataEntity userdata = userDataRepository.findByEmail(form.getEmail());
        if(userdata == null) throw new ItemNotFoundException("Not Found User");
        ShareBoard shareBoardCollab = repository.findById(new ShareBoardId(userdata.getOid(),board)).orElse(null);

        if(shareBoardCollab != null && shareBoardCollab.getRole().equals(ShareBoardsRole.OWNER)) throw new ConflictException("Board owner cannot be collaborator of his/her own board.");
        ShareBoard userAdded = repository.findById(new ShareBoardId(userdata.getOid(),board)).orElse(null);
        RequestCollab userInvited = requestCollabRepository.findById(new RequestCollabId(userdata.getOid(),board)).orElse(null);
        if(userAdded != null || userInvited != null) throw new ConflictException("The user is already the collaborator or Invited of this board.");
        if(!shareBoard.getRole().equals(ShareBoardsRole.OWNER)) throw new NoAccessException("The owner only is allow for this action!!");


        RequestCollab newRequestCollab = new RequestCollab();
        ShareBoardsRole role = form.getAccessRight().equals("WRITE") ? ShareBoardsRole.WRITER : ShareBoardsRole.READER;
        newRequestCollab.setRole(role);
        newRequestCollab.setBoard(board);
        newRequestCollab.setOidUserShare(userdata.getOid());
        RequestCollaboratorDto result =  mapper.map(requestCollabRepository.save(newRequestCollab),RequestCollaboratorDto.class);
        result.setName(userdata.getName());
        result.setEmail(userdata.getEmail());
        result.setStatus("PADDING");
        return  result;
    }
}
