package com.example.itbangmodkradankanbanapi.services.V2;

import com.example.itbangmodkradankanbanapi.dtos.V2.FormTaskDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V2.FullTaskDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V2.TaskDtoV2;
import com.example.itbangmodkradankanbanapi.entities.V2.karban.Setting;
import com.example.itbangmodkradankanbanapi.entities.V2.karban.StatusV2;
import com.example.itbangmodkradankanbanapi.entities.V2.karban.TasksV2;
import com.example.itbangmodkradankanbanapi.exceptions.InvalidFieldInputException;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.exceptions.NotAllowedException;
import com.example.itbangmodkradankanbanapi.models.SettingLockStatus;
import com.example.itbangmodkradankanbanapi.repositories.V2.karban.StatusRepositoryV2;
import com.example.itbangmodkradankanbanapi.repositories.V2.karban.TaskRepositoryV2;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TaskServiceV2 {
    @Autowired
    private TaskRepositoryV2 repository;
    @Autowired
    private StatusRepositoryV2 statusRepository;
    @Autowired
    private SettingService settingService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ListMapper listMapper;


    public FullTaskDtoV2 getTask(Integer id){
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Task "+ id + " dose not exist !!!!")),FullTaskDtoV2.class);
    }


    @Transactional
    public TaskDtoV2 deleteTask(Integer id){

      TasksV2 task =  repository.findById(id).orElseThrow(() -> new ItemNotFoundException("NOT FOUND"));
          repository.delete(task);
        return modelMapper.map(task,TaskDtoV2.class);
    }


    public List<TaskDtoV2> getAllTaskByStatusIdIn(String[] filterStatuses, String[] sortBy,String[] direction) {

        List<Sort.Order> orders = new ArrayList<>();
        if((sortBy.length != 0 && !sortBy[0].equals("status.name"))|| sortBy.length > 1 ) throw new NotAllowedException("invalid filter parameter");
        else if(sortBy.length !=0)
            for (int i = 0; i < sortBy.length; i++) {
                orders.add(new Sort.Order((direction[i].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC), sortBy[i]));
            }
        else orders.add(new Sort.Order(Sort.Direction.ASC ,"createdOn"));
        if(filterStatuses.length == 0) return  listMapper.mapList(repository.findAll(Sort.by(orders)),TaskDtoV2.class);

        List<StatusV2> statuses = Arrays.stream(filterStatuses).map((filterStatus) -> statusRepository.findByName(filterStatus.replace("_"," "))).toList();
        return listMapper.mapList(repository.findByStatusIn(statuses,Sort.by(orders)),TaskDtoV2.class);
    }



    public TaskDtoV2 updateTask(Integer id, FormTaskDtoV2 formTask){

        TasksV2 updateTask = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found"));
        StatusV2 oldStatus = updateTask.getStatus();
        updateTask.setTitle(formTask.getTitle());
        updateTask.setAssignees(formTask.getAssignees());
        updateTask.setDescription(formTask.getDescription());
        StatusV2 status = statusRepository.findById(formTask.getStatusId()).orElseThrow(() -> new InvalidFieldInputException("status","does not exist"));
        Setting setting =settingService.getSetting("limit_of_tasks");
        if(!SettingLockStatus.isLockStatusId(status.getId()) &&  setting.getEnable() && (status.getTasks().size() >= setting.getValue() && !oldStatus.equals(status))) throw new InvalidFieldInputException("status","the status has reached the limit");
        updateTask.setStatus(status);
      return  modelMapper.map( repository.save(updateTask),TaskDtoV2.class);
    }

    @Transactional
    public TaskDtoV2 addTask(FormTaskDtoV2 formTask){
        TasksV2 newTask = new TasksV2();
        newTask.setTitle(formTask.getTitle());
        newTask.setAssignees(formTask.getAssignees());
        newTask.setDescription(formTask.getDescription());
        StatusV2 status = statusRepository.findById(formTask.getStatusId()).orElseThrow(() -> new InvalidFieldInputException("status","does not exist"));
        Setting setting =settingService.getSetting("limit_of_tasks");
        if(!SettingLockStatus.isLockStatusId(status.getId()) &&setting.getEnable() && status.getTasks().size() >= setting.getValue()) throw new InvalidFieldInputException("status","the status has reached the limit");
        newTask.setStatus(status);
        return modelMapper.map( repository.save(newTask),TaskDtoV2.class);
    }
}
