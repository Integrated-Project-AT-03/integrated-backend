package com.example.itbangmodkradankanbanapi.services.V2;


import com.example.itbangmodkradankanbanapi.dtos.V2.FormStatusDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V2.FullStatusDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V2.StatusDtoV2;
import com.example.itbangmodkradankanbanapi.entities.V2.karban.Setting;
import com.example.itbangmodkradankanbanapi.entities.V2.karban.StatusV2;
import com.example.itbangmodkradankanbanapi.entities.V2.karban.TasksV2;
import com.example.itbangmodkradankanbanapi.exceptions.InvalidFieldInputException;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.exceptions.NotAllowedException;
import com.example.itbangmodkradankanbanapi.models.SettingLockStatus;
import com.example.itbangmodkradankanbanapi.repositories.V2.karban.ColorRepository;
import com.example.itbangmodkradankanbanapi.repositories.V2.karban.StatusRepositoryV2;
import com.example.itbangmodkradankanbanapi.repositories.V2.karban.TaskRepositoryV2;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceV2 {
    @Autowired
    private StatusRepositoryV2 repository;
    @Autowired
    private TaskRepositoryV2 taskRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private SettingService settingService;



    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ListMapper listMapper;

    public FullStatusDtoV2 getStatus(Integer id) {
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Status " + id + " dose not exist !!!!")), FullStatusDtoV2.class);
    }


    public List<StatusDtoV2> getAllStatus() {
        return listMapper.mapList(repository.findAll(), StatusDtoV2.class);
    }
    @Transactional

    public StatusDtoV2 updateStatus(Integer id, FormStatusDtoV2 status) {
            StatusV2 updatedStatus = repository.findById(id).orElseThrow(() ->  new ItemNotFoundException("Status " + id + " dose not exist !!!!"));
        if(SettingLockStatus.isLockStatusId(id)) throw new NotAllowedException(updatedStatus.getName().toLowerCase() + " cannot be modified.");
           else if(repository.findByName(status.getName()) != null &&!status.getName().equals(updatedStatus.getName())) throw new InvalidFieldInputException("name","must be unique");
            updatedStatus.setName(status.getName());
            updatedStatus.setStatusDescription(status.getStatusDescription());
            updatedStatus.setColor(colorRepository.findById(status.getColorId()).orElseThrow(() -> new ItemNotFoundException("Color " + status.getColorId() + " dose not exist !!!!")));
            return modelMapper.map(repository.save(updatedStatus), StatusDtoV2.class);
    }

    @Transactional
    public StatusDtoV2 addStatus(FormStatusDtoV2 status) {
            if(repository.findByName(status.getName()) != null) throw new InvalidFieldInputException("name","must be unique");
            StatusV2 newStatus = new StatusV2();
            newStatus.setName(status.getName());
            newStatus.setStatusDescription(status.getStatusDescription());
            newStatus.setColor(colorRepository.findById(status.getColorId()).orElseThrow(() -> new ItemNotFoundException("Color " + status.getColorId() + " dose not exist !!!!")));
            return modelMapper.map(repository.save(newStatus), StatusDtoV2.class);
    }

    @Transactional
    public StatusDtoV2 deleteStatus(Integer id) {
        StatusV2 status =repository.findById(id).orElseThrow(() -> new ItemNotFoundException("NOT FOUND"));
        if(SettingLockStatus.isLockStatusId(id)) throw new NotAllowedException(status.getName().toLowerCase()  + " cannot be deleted.");
       else if(status.getTasks().size() != 0) throw new InvalidFieldInputException("status","Cannot Delete a status that still have tasks");
       repository.delete(status);
        return modelMapper.map(status, StatusDtoV2.class);
    }
    @Transactional
    public Integer ChangeTasksByStatusAndDelete(Integer deletedStatusId, Integer changeStatusId){
        StatusV2 deletedStatus = repository.findById(deletedStatusId).orElseThrow(()-> new NotAllowedException("destination status for task transfer not specified."));
        StatusV2 changeStatus = repository.findById(changeStatusId).orElseThrow(()-> new NotAllowedException("the specified status for task transfer does not exist"));
        if(SettingLockStatus.isLockStatusId(deletedStatus.getId())) throw new NotAllowedException(deletedStatus.getName().toLowerCase()  + " cannot be deleted.");
        if(deletedStatus.equals(changeStatus)) throw new NotAllowedException("destination status for task transfer must be different from current status");
        List<TasksV2> tasks = deletedStatus.getTasks();
        Setting setting =settingService.getSetting("limit_of_tasks");
        if(!SettingLockStatus.isLockStatusId(changeStatus.getId()) && setting.getEnable() && changeStatus.getTasks().size() + tasks.size() > setting.getValue()) throw new InvalidFieldInputException("status","the destination status cannot be over the limit after transfer");
        List<TasksV2> updatedTasks = tasks.stream().peek((task -> {
            task.setStatus(changeStatus);
        } )).toList();
        taskRepository.saveAll(updatedTasks);
        repository.delete(deletedStatus);
        return updatedTasks.size();
    }
}
