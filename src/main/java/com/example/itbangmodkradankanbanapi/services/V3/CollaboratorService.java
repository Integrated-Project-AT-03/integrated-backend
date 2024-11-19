package com.example.itbangmodkradankanbanapi.services.V3;

import com.example.itbangmodkradankanbanapi.Auth.JwtTokenUtil;
import com.example.itbangmodkradankanbanapi.dtos.V3.collaborator.*;
import com.example.itbangmodkradankanbanapi.dtos.V3.mail.FormMailDto;
import com.example.itbangmodkradankanbanapi.entities.V3.*;
import com.example.itbangmodkradankanbanapi.entities.user.UserdataEntity;
import com.example.itbangmodkradankanbanapi.exceptions.*;
import com.example.itbangmodkradankanbanapi.repositories.V3.BoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.RequestCollabRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.ShareBoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.user.UserDataCenterRepository;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import com.fasterxml.jackson.databind.JsonNode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private UserDataCenterRepository userDataCenterRepository;
    @Autowired
    private BoardRepositoryV3 boardRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private MailService mailService;
    @Autowired
    private MISLService mislService;

    @Value("${jwt.access.token.cookie.name}")
    private String jwtCookie;
    @Value("${jwt.ref.access.token.cookie.name}")
    private String jwtRefCookie;
    @Value("${microsoft.access.token.cookie.name}")
    private String microsoftAccessToken;




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
           UserdataEntity userdata = userDataCenterRepository.findById(collab.getOid()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
            collab.setName(userdata.getName());
            collab.setEmail(userdata.getEmail());
            collab.setStatus("ACTIVE");
       }).toList());

        tempCollaboratorsDto.addAll(listMapper.mapList(requestCollabs,CollaboratorDto.class).stream().peek((requestCollab)-> {
            UserdataEntity userdata = userDataCenterRepository.findById(requestCollab.getOid()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
            requestCollab.setName(userdata.getName());
            requestCollab.setEmail(userdata.getEmail());
            requestCollab.setStatus("PENDING");
        }).toList());

        return  tempCollaboratorsDto;
    }

    public CollaboratorDto getCollaboratorByNanoIdAndOid(String nanoId,String oid){
        Board board = boardRepository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Not Found Boards"));
        ShareBoard shareBoard = repository.findByBoardAndOidUserShareAndRoleNot(board,oid,ShareBoardsRole.OWNER);
        if(shareBoard == null) throw  new ItemNotFoundException("Not found this collaborator");
        CollaboratorDto collaborator = mapper.map(shareBoard,CollaboratorDto.class);
        UserdataEntity userdata = userDataCenterRepository.findById(collaborator.getOid()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
        collaborator.setName(userdata.getName());
        collaborator.setEmail(userdata.getEmail());
        return  collaborator;
    }

    public RequestCollaboratorDto getInviteById(String nanoId,String oid){
        Board board = boardRepository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Not Found Boards"));
        RequestCollab requestCollab = requestCollabRepository.findFirstByBoardAndOidUserShare(board,oid);
        if(requestCollab == null) throw  new ItemNotFoundException("Not found this request");
        RequestCollaboratorDto requestCollaboratorDto = mapper.map(requestCollab,RequestCollaboratorDto.class);
        UserdataEntity userdata = userDataCenterRepository.findById(requestCollaboratorDto.getOid()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
        requestCollaboratorDto.setName(userdata.getName());
        requestCollaboratorDto.setEmail(userdata.getEmail());
        return  requestCollaboratorDto;
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
    public Map<String,String> updateInviteCollab(String nanoId,String oid, UpdateInviteCollaboratorDto form){
        Board board = boardRepository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Not Found Boards"));
        RequestCollab inviteBoard = requestCollabRepository.findById(new RequestCollabId(oid,board)).orElseThrow(() -> new NoSuchElementException("not found invite for this board"));
        if(!form.getAccessRight().equals("WRITE") && !form.getAccessRight().equals("READ")  )  throw new NotAllowedException("Only Add Read or Write");
        ShareBoardsRole role = form.getAccessRight().equals("WRITE") ? ShareBoardsRole.WRITER : ShareBoardsRole.READER;
        inviteBoard.setRole(role);
        requestCollabRepository.save(inviteBoard);
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

        List<RequestCollab> inviteBoards = requestCollabRepository.findAllByOidUserShare(oid,Sort.by(orders));

        AtomicInteger atomicIntegerShareBoards = new AtomicInteger(0);
        AtomicInteger atomicInviteBoards = new AtomicInteger(0);
        List<CollaboratorBoardDto> tempCollaboratorBoardDtos  =new ArrayList<>() ;

        tempCollaboratorBoardDtos.addAll(
         listMapper.mapList(inviteBoards,CollaboratorBoardDto.class).stream().peek((inviteBoard)-> {
            ShareBoard shareBoardOwner = repository.findByBoardAndRole(inviteBoards.get(  atomicInviteBoards.getAndIncrement()).getBoard(),ShareBoardsRole.OWNER);
            UserdataEntity userdata = userDataCenterRepository.findById(shareBoardOwner.getOidUserShare()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
             inviteBoard.setName(userdata.getName());
             inviteBoard.setEmail(userdata.getEmail());
             inviteBoard.setStatus("PENDING");
        }).toList());

        tempCollaboratorBoardDtos.addAll(
                listMapper.mapList(shareBoards,CollaboratorBoardDto.class).stream().peek((shareBoard)-> {
                    ShareBoard shareBoardOwner = repository.findByBoardAndRole(shareBoards.get( atomicIntegerShareBoards.getAndIncrement()).getBoard(),ShareBoardsRole.OWNER);
                    UserdataEntity userdata = userDataCenterRepository.findById(shareBoardOwner.getOidUserShare()).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
                    shareBoard.setName(userdata.getName());
                    shareBoard.setEmail(userdata.getEmail());
                    shareBoard.setStatus("ACTIVE");
                }).toList());
        return tempCollaboratorBoardDtos;
    }



    @Transactional
    public CollaboratorDto receiveCollaborator(HttpServletRequest request,FormReceiveCollaboratorDto form){
      Board board = boardRepository.findById(form.getBoardNanoId()).orElseThrow(()-> new ItemNotFoundException("Not Found Boards: "+ form.getBoardNanoId()));
       String token = jwtTokenUtil.getTokenCookie(request.getCookies());
       String oid = jwtTokenUtil.getAllClaimsFromToken(token).get("oid").toString();
       RequestCollab requestCollab =  requestCollabRepository.findById(new RequestCollabId(oid,board)).orElseThrow(()-> new ItemNotFoundException("You not invited for this board"));
        UserdataEntity userdata = userDataCenterRepository.findById(oid).orElseThrow(()-> new ItemNotFoundException("Not Found User"));

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
        UserdataEntity userdata = userDataCenterRepository.findById(oid).orElseThrow(()-> new ItemNotFoundException("Not Found User"));
        RequestCollab requestCollab =  requestCollabRepository.findById(new RequestCollabId(oid,board)).orElseThrow(()-> new ItemNotFoundException(" Not found invite for this board"));
        RequestCollaboratorDto result = mapper.map(requestCollab, RequestCollaboratorDto.class);
        requestCollabRepository.delete(requestCollab);
        result.setName(userdata.getName());
        result.setEmail(userdata.getEmail());
        result.setStatus("CANCEL");
        return  result;
    }



    @Transactional
    public RequestCollaboratorDto inviteCollaborator(HttpServletRequest request, String nanoId,FormCollaboratorDto form)  {
        Board board = boardRepository.findById(nanoId).orElseThrow(()-> new ItemNotFoundException("Not Found Boards"));
        Map<String,String> cookieMap = jwtTokenUtil.getMapCookie(request.getCookies());
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(cookieMap.get(jwtCookie));
        ShareBoard shareBoard = repository.findById(new ShareBoardId(claims.get("oid").toString(),board)).orElseThrow(()-> new ItemNotFoundException("Not Found this user in board by Token"));
        JsonNode userMicrosoft = null;
        UserdataEntity userdata = null;
        if(cookieMap.containsKey(microsoftAccessToken)){
            try {
                System.out.println("find by microsoft");
                userMicrosoft = mislService.findUserByEmail(form.getEmail(),cookieMap.get(microsoftAccessToken));
            }catch (Exception er){
                System.out.println("Not found in microsoft try to center data!!");
                throw new ItemNotFoundException("Not found in microsoft try to center data!!" + er.getMessage());
            }
        }

        if(userMicrosoft == null ) {
            userdata = userDataCenterRepository.findByEmail(form.getEmail());
            if(userdata == null) throw new ItemNotFoundException("Not Found User");

        }
        boolean isMicrosoft = userMicrosoft!=null;
        String userOid = isMicrosoft ? userMicrosoft.get("id").asText(): userdata.getOid() ;
        String userName = isMicrosoft ? userMicrosoft.get("displayName").asText():userdata.getName();
        String userEmail= isMicrosoft ? userMicrosoft.get("userPrincipalName").asText(): userdata.getEmail();

        ShareBoard shareBoardCollab = repository.findById(new ShareBoardId(userOid,board)).orElse(null);
        if(shareBoardCollab != null && shareBoardCollab.getRole().equals(ShareBoardsRole.OWNER)) throw new ConflictException("Board owner cannot be collaborator of his/her own board.");

        ShareBoard userAdded = repository.findById(new ShareBoardId(userOid ,board)).orElse(null);
        RequestCollab userInvited = requestCollabRepository.findById(new RequestCollabId(userOid,board)).orElse(null);
        if(userAdded != null || userInvited != null) throw new ConflictException("The user is already the collaborator or pending collaborator of this board.");
        if(!shareBoard.getRole().equals(ShareBoardsRole.OWNER)) throw new NoAccessException("The owner only is allow for this action!!");
        RequestCollab newRequestCollab = new RequestCollab();
        ShareBoardsRole role = form.getAccessRight().equals("WRITE") ? ShareBoardsRole.WRITER : ShareBoardsRole.READER;
        newRequestCollab.setRole(role);
        newRequestCollab.setBoard(board);
        newRequestCollab.setOidUserShare(userOid);
        RequestCollaboratorDto result =  mapper.map(requestCollabRepository.save(newRequestCollab),RequestCollaboratorDto.class);
        result.setName(userName);
        result.setEmail(userEmail);
        result.setStatus("PENDING");

        FormMailDto formMailDto = mapper.map(newRequestCollab,FormMailDto.class);
        formMailDto.setRecipientEmail(form.getEmail());
        formMailDto.setTo(userName);
        formMailDto.setFrom(claims.get("name").toString());
        Boolean isSendEmail = mailService.sendInvitationEmail(formMailDto);
        result.setIsSendEmail(isSendEmail);
        return  result;
    }
}
