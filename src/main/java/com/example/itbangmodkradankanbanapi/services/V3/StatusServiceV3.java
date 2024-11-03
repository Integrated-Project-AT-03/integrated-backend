package com.example.itbangmodkradankanbanapi.services.V3;


import com.example.itbangmodkradankanbanapi.dtos.V3.status.FormStatusDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.status.FullStatusDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.status.StatusDtoV3;
import com.example.itbangmodkradankanbanapi.entities.V3.Board;
import com.example.itbangmodkradankanbanapi.entities.V3.StatusV3;
import com.example.itbangmodkradankanbanapi.entities.V3.TasksV3;
import com.example.itbangmodkradankanbanapi.exceptions.InvalidFieldInputException;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.exceptions.NotAllowedException;
import com.example.itbangmodkradankanbanapi.repositories.V2.ColorRepository;
import com.example.itbangmodkradankanbanapi.repositories.V3.BoardRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.StatusRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.TaskRepositoryV3;
import com.example.itbangmodkradankanbanapi.services.V2.SettingService;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class StatusServiceV3 {
    @Autowired
    private StatusRepositoryV3 repository;
    @Autowired
    private TaskRepositoryV3 taskRepository;

    @Autowired
    private BoardRepositoryV3 boardRepository;

    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private SettingService settingService;


    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ListMapper listMapper;

    public FullStatusDtoV3 getStatus(Integer id) {
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Status " + id + " dose not exist !!!!")), FullStatusDtoV3.class);
    }



    private void closeEnableStatusCenterByStatus(Board board, StatusV3 targetStatus){
        AtomicInteger index = new AtomicInteger(1);
        String updateEnableCenterStatus = Arrays.stream(board.getEnableStatusCenter().split("")).map((bit -> index.getAndIncrement() == targetStatus.getId() ? "0" : bit)).collect(Collectors.joining(""));
        board.setEnableStatusCenter(updateEnableCenterStatus);
        boardRepository.save(board);
    }



    @Transactional
    public StatusDtoV3 updateStatus(Integer id, FormStatusDtoV3 statusForm,String nanoIdBoard) {
        StatusV3 targetStatus = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Status " + id + " dose not exist !!!!"));
        Board board = boardRepository.findById(nanoIdBoard).orElseThrow(() -> new ItemNotFoundException("board " + statusForm.getBoardNanoId() + " dose not exist !!!!"));

        if (repository.findByNameAndBoard(statusForm.getName(),board) != null && !statusForm.getName().equals(targetStatus.getName()))
            throw new InvalidFieldInputException("name", "must be unique");

        if (targetStatus.getCenterStatus() != null) {
            if (!targetStatus.getCenterStatus().getEnableConfig())
                throw new NotAllowedException(targetStatus.getName().toLowerCase() + " cannot be modified.");


            statusForm.setBoardNanoId(board.getNanoIdBoard());
            closeEnableStatusCenterByStatus(board,targetStatus);
            StatusV3 newStatus = new StatusV3();
            newStatus.setName(statusForm.getName());
            newStatus.setColor(targetStatus.getColor());
            newStatus.setStatusDescription(statusForm.getStatusDescription());
            newStatus.setColor(colorRepository.findById(statusForm.getColorId()).orElseThrow(() -> new ItemNotFoundException("Color " + statusForm.getColorId() + " dose not exist !!!!")));
            newStatus.setBoard(board);
            newStatus.setUpdatedOn(targetStatus.getUpdatedOn());
            newStatus.setCreatedOn(targetStatus.getCreatedOn());
            StatusV3 cloneStatus = repository.save(newStatus);

          List<TasksV3> transferTask =  taskRepository.findAllByStatusAndBoard(targetStatus,board).stream().peek(task -> {
                task.setStatus(cloneStatus);
            }).toList();
          taskRepository.saveAll(transferTask);


            return modelMapper.map(repository.save(cloneStatus), StatusDtoV3.class);
        }
        targetStatus.setName(statusForm.getName());
        targetStatus.setStatusDescription(statusForm.getStatusDescription());
        targetStatus.setColor(colorRepository.findById(statusForm.getColorId()).orElseThrow(() -> new ItemNotFoundException("Color " + statusForm.getColorId() + " dose not exist !!!!")));
        return modelMapper.map(repository.save(targetStatus), StatusDtoV3.class);
    }

    @Transactional
    public StatusDtoV3 addStatus(FormStatusDtoV3 statusForm,String nanoIdBoard) {
        Board board = boardRepository.findById(nanoIdBoard).orElseThrow(() -> new ItemNotFoundException("board " + statusForm.getBoardNanoId() + " dose not exist !!!!"));

        if (repository.findByNameAndBoard(statusForm.getName(), board) != null)
            throw new InvalidFieldInputException("name", "must be unique");
        StatusV3 newStatus = new StatusV3();
        newStatus.setName(statusForm.getName());
        newStatus.setStatusDescription(statusForm.getStatusDescription());
        newStatus.setColor(colorRepository.findById(statusForm.getColorId()).orElseThrow(() -> new ItemNotFoundException("Color " + statusForm.getColorId() + " dose not exist !!!!")));
        newStatus.setBoard(board);
        return modelMapper.map(repository.save(newStatus), StatusDtoV3.class);
    }

    @Transactional
    public StatusDtoV3 deleteStatus(Integer idStatus,String nanoIdBoard) {
        Board board = boardRepository.findById(nanoIdBoard).orElseThrow(() -> new ItemNotFoundException("board " + nanoIdBoard + " dose not exist !!!!"));
        StatusV3 targetStatus = repository.findById(idStatus).orElseThrow(() -> new ItemNotFoundException("NOT FOUND"));
        if (targetStatus.getCenterStatus() != null && !targetStatus.getCenterStatus().getEnableConfig() )
            throw new NotAllowedException(targetStatus.getName().toLowerCase() + " cannot be deleted.");

        if (!taskRepository.findAllByStatusAndBoard(targetStatus, board).isEmpty())
            throw new InvalidFieldInputException("status", "Cannot Delete a status that still have tasks");

        if (targetStatus.getCenterStatus() != null) {
            closeEnableStatusCenterByStatus(board,targetStatus);
            return modelMapper.map(targetStatus, StatusDtoV3.class);
        }

            repository.delete(targetStatus);
        return modelMapper.map(targetStatus, StatusDtoV3.class);
    }


    @Transactional
    public Integer ChangeTasksByStatusAndDelete(Integer deletedStatusId, Integer changeStatusId,String nanoIdBoard) {
        Board board = boardRepository.findById(nanoIdBoard).orElseThrow(() -> new ItemNotFoundException("board " + nanoIdBoard + " dose not exist !!!!"));
        StatusV3 deletedStatus = repository.findById(deletedStatusId).orElseThrow(() -> new NotAllowedException("destination status for task transfer not specified."));
        StatusV3 changeStatus = repository.findById(changeStatusId).orElseThrow(() -> new NotAllowedException("the specified status for task transfer does not exist"));

        if (deletedStatus.getCenterStatus() != null && !deletedStatus.getCenterStatus().getEnableConfig() )
            throw new NotAllowedException(deletedStatus.getName().toLowerCase() + " cannot be deleted.");


        if (deletedStatus.equals(changeStatus))
            throw new NotAllowedException("destination status for task transfer must be different from current status");

        List<TasksV3> transferTask =  taskRepository.findAllByStatusAndBoard(deletedStatus,board).stream().peek(task -> {
            task.setStatus(changeStatus);
        }).toList();

        if(changeStatus.getCenterStatus() != null) {
            if (changeStatus.getCenterStatus().getEnableConfig() && board.getEnableLimitsTask() && changeStatus.getTasks().size() + transferTask.size() > board.getLimitsTask())
                throw new InvalidFieldInputException("status", "the destination status cannot be over the limit after transfer");
        }else {
            if (board.getEnableLimitsTask() && changeStatus.getTasks().size() + transferTask.size() > board.getLimitsTask())
                throw new InvalidFieldInputException("status", "the destination status cannot be over the limit after transfer");
        }

        if(deletedStatus.getCenterStatus() != null){
            taskRepository.saveAll(transferTask);
            closeEnableStatusCenterByStatus(board,deletedStatus);
            return  transferTask.size();
        }

        taskRepository.saveAll(transferTask);
        repository.delete(deletedStatus);
        return transferTask.size();
    }
}
