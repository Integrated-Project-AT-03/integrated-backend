package com.example.itbangmodkradankanbanapi.services.V3;


import com.example.itbangmodkradankanbanapi.dtos.V3.status.FormStatusDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.status.FullStatusDtoV3;
import com.example.itbangmodkradankanbanapi.dtos.V3.status.StatusDtoV3;
import com.example.itbangmodkradankanbanapi.entities.V2.Setting;
import com.example.itbangmodkradankanbanapi.entities.V3.StatusV3;
import com.example.itbangmodkradankanbanapi.entities.V3.TasksV3;
import com.example.itbangmodkradankanbanapi.exceptions.InvalidFieldInputException;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.exceptions.NotAllowedException;
import com.example.itbangmodkradankanbanapi.models.SettingLockStatus;
import com.example.itbangmodkradankanbanapi.repositories.V2.ColorRepository;
import com.example.itbangmodkradankanbanapi.repositories.V3.StatusRepositoryV3;
import com.example.itbangmodkradankanbanapi.repositories.V3.TaskRepositoryV3;
import com.example.itbangmodkradankanbanapi.services.V2.SettingService;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceV3 {
    @Autowired
    private StatusRepositoryV3 repository;
    @Autowired
    private TaskRepositoryV3 taskRepository;
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


    public List<StatusDtoV3> getAllStatus() {
        return listMapper.mapList(repository.findAll(), StatusDtoV3.class);
    }
    @Transactional

    public StatusDtoV3 updateStatus(Integer id, FormStatusDtoV3 status) {
            StatusV3 updatedStatus = repository.findById(id).orElseThrow(() ->  new ItemNotFoundException("Status " + id + " dose not exist !!!!"));
        if(SettingLockStatus.isLockStatusId(id)) throw new NotAllowedException(updatedStatus.getName().toLowerCase() + " cannot be modified.");
           else if(repository.findByName(status.getName()) != null &&!status.getName().equals(updatedStatus.getName())) throw new InvalidFieldInputException("name","must be unique");
            updatedStatus.setName(status.getName());
            updatedStatus.setStatusDescription(status.getStatusDescription());
            updatedStatus.setColor(colorRepository.findById(status.getColorId()).orElseThrow(() -> new ItemNotFoundException("Color " + status.getColorId() + " dose not exist !!!!")));
            return modelMapper.map(repository.save(updatedStatus), StatusDtoV3.class);
    }

    @Transactional
    public StatusDtoV3 addStatus(FormStatusDtoV3 status) {
            if(repository.findByName(status.getName()) != null) throw new InvalidFieldInputException("name","must be unique");
            StatusV3 newStatus = new StatusV3();
            newStatus.setName(status.getName());
            newStatus.setStatusDescription(status.getStatusDescription());
            newStatus.setColor(colorRepository.findById(status.getColorId()).orElseThrow(() -> new ItemNotFoundException("Color " + status.getColorId() + " dose not exist !!!!")));
            return modelMapper.map(repository.save(newStatus), StatusDtoV3.class);
    }

    @Transactional
    public StatusDtoV3 deleteStatus(Integer id) {
        StatusV3 status =repository.findById(id).orElseThrow(() -> new ItemNotFoundException("NOT FOUND"));
        if(SettingLockStatus.isLockStatusId(id)) throw new NotAllowedException(status.getName().toLowerCase()  + " cannot be deleted.");
       else if(status.getTasks().size() != 0) throw new InvalidFieldInputException("status","Cannot Delete a status that still have tasks");
       repository.delete(status);
        return modelMapper.map(status, StatusDtoV3.class);
    }
    @Transactional
    public Integer ChangeTasksByStatusAndDelete(Integer deletedStatusId, Integer changeStatusId){
        StatusV3 deletedStatus = repository.findById(deletedStatusId).orElseThrow(()-> new NotAllowedException("destination status for task transfer not specified."));
        StatusV3 changeStatus = repository.findById(changeStatusId).orElseThrow(()-> new NotAllowedException("the specified status for task transfer does not exist"));
        if(SettingLockStatus.isLockStatusId(deletedStatus.getId())) throw new NotAllowedException(deletedStatus.getName().toLowerCase()  + " cannot be deleted.");
        if(deletedStatus.equals(changeStatus)) throw new NotAllowedException("destination status for task transfer must be different from current status");
        List<TasksV3> tasks = deletedStatus.getTasks();
        Setting setting =settingService.getSetting("limit_of_tasks");
        if(!SettingLockStatus.isLockStatusId(changeStatus.getId()) && setting.getEnable() && changeStatus.getTasks().size() + tasks.size() > setting.getValue()) throw new InvalidFieldInputException("status","the destination status cannot be over the limit after transfer");
        List<TasksV3> updatedTasks = tasks.stream().peek((task -> {
            task.setStatus(changeStatus);
        } )).toList();
        taskRepository.saveAll(updatedTasks);
        repository.delete(deletedStatus);
        return updatedTasks.size();
    }
}
