package com.example.itbangmodkradankanbanapi.services.V2;


import com.example.itbangmodkradankanbanapi.dtos.V2.FormStatusDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V2.FullStatusDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V2.StatusDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V2.TaskDtoV2;
import com.example.itbangmodkradankanbanapi.entities.V2.StatusV2;
import com.example.itbangmodkradankanbanapi.entities.V2.TasksV2;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.models.Settings;
import com.example.itbangmodkradankanbanapi.repositories.V2.ColorRepository;
import com.example.itbangmodkradankanbanapi.repositories.V2.StatusRepositoryV2;
import com.example.itbangmodkradankanbanapi.repositories.V2.TaskRepositoryV2;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StatusServiceV2 {
    @Autowired
    private StatusRepositoryV2 repository;
    @Autowired
    private TaskRepositoryV2 taskRepository;
    @Autowired
    private ColorRepository colorRepository;
    private final Settings settings = new Settings();


    @Autowired
    private ListMapper listMapper;

    private final ModelMapper modelMapper = new ModelMapper();

    public FullStatusDtoV2 getStatus(Integer id) {
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Status " + id + " dose not exist !!!!")), FullStatusDtoV2.class);
    }


    public List<StatusDtoV2> getAllStatus() {
        return listMapper.mapList(repository.findAll(), StatusDtoV2.class);
    }
    @Transactional

    public StatusDtoV2 updateStatus(Integer id, FormStatusDtoV2 status) {
        if(id == 1 ) throw new DataIntegrityViolationException("Can't not edit No Status");
        else if(id == 4) throw new DataIntegrityViolationException("Can't not edit Done");
        try {
            StatusV2 updatedStatus = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("NOT FOUND"));
            updatedStatus.setStatusName(status.getStatusName());
            updatedStatus.setStatusDescription(status.getStatusDescription());
            updatedStatus.setColor(colorRepository.findById(status.getColorId()).orElseThrow(() -> new ItemNotFoundException("Color " + status.getColorId() + " dose not exist !!!!")));
            return modelMapper.map(repository.save(updatedStatus), StatusDtoV2.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("could not execute statement [Duplicate entry " + status.getStatusName() + " for key 'statuses.name_UNIQUE'] (description,name) value (?,?)]; constraint [statuses.name_UNIQUE]");
        }
    }

    @Transactional
    public StatusDtoV2 addStatus(FormStatusDtoV2 status) {
        try {
            StatusV2 newStatus = new StatusV2();
            newStatus.setStatusName(status.getStatusName());
            newStatus.setStatusDescription(status.getStatusDescription());
            newStatus.setColor(colorRepository.findById(status.getColorId()).orElseThrow(() -> new NoSuchElementException("Color " + status.getColorId() + " dose not exist !!!!")));
            return modelMapper.map(repository.save(newStatus), StatusDtoV2.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }

    @Transactional
    public StatusDtoV2 deleteStatus(Integer id) {
        if(id == 1 ) throw new DataIntegrityViolationException("Can't not delete No Status");
        else if(id == 4) throw new DataIntegrityViolationException("Can't not delete Done");
        StatusV2 task;
        task = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("NOT FOUND"));
        try {
            repository.delete(task);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }
        return modelMapper.map(task, StatusDtoV2.class);
    }
    @Transactional
    public Integer ChangeTasksByStatusAndDelete(Integer deletedStatusId, Integer changeStatusId){
        if(deletedStatusId == 1 ) throw new DataIntegrityViolationException("Can't not delete No Status");
        else if(deletedStatusId == 4) throw new DataIntegrityViolationException("Can't not delete Done");
        StatusV2 deletedStatus = repository.findById(deletedStatusId).orElseThrow(()-> new ItemNotFoundException("Deleted status is not exist"));
        StatusV2 changeStatus = repository.findById(changeStatusId).orElseThrow(()-> new ItemNotFoundException("Change status is not exist"));
        List<TasksV2> tasks = deletedStatus.getTasks();
        if(changeStatus.getLimitMaximumTask() && changeStatus.getTasks().size() + tasks.size() > settings.getNumOfLimitsTask()) throw new DataIntegrityViolationException("The status " + changeStatus.getStatusName() + " will have too many tasks");
        List<TasksV2> updatedTasks = tasks.stream().peek((task -> {
            task.setStatus(changeStatus);
        } )).toList();
        taskRepository.saveAll(updatedTasks);
        this.deleteStatus(deletedStatusId);
        return updatedTasks.size();
    }
}
