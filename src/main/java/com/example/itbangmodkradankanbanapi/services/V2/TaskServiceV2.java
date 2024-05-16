package com.example.itbangmodkradankanbanapi.services.V2;

import com.example.itbangmodkradankanbanapi.dtos.V2.FormTaskDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V2.FullTaskDtoV2;
import com.example.itbangmodkradankanbanapi.dtos.V2.TaskDtoV2;
import com.example.itbangmodkradankanbanapi.entities.V2.StatusV2;
import com.example.itbangmodkradankanbanapi.entities.V2.TasksV2;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.repositories.V2.StatusRepositoryV2;
import com.example.itbangmodkradankanbanapi.repositories.V2.TaskRepositoryV2;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskServiceV2 {
    @Autowired
    private TaskRepositoryV2 repository;
    @Autowired
    private StatusRepositoryV2 statusRepository;


    @Autowired
    private ListMapper listMapper;
    private final ModelMapper modelMapper = new ModelMapper();
    public FullTaskDtoV2 getTask(Integer id){
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new NoSuchElementException("Task "+ id + " dose not exist !!!!")),FullTaskDtoV2.class);
    }

    public List<TaskDtoV2> getAllTask(){
        return listMapper.mapList(repository.findAllByOrderByCreatedOnAsc(),TaskDtoV2.class);
    }

    @Transactional
    public TaskDtoV2 deleteTask(Integer id){

      TasksV2 task =  repository.findById(id).orElseThrow(() -> new ItemNotFoundException("NOT FOUND"));
          repository.delete(task);
        return modelMapper.map(task,TaskDtoV2.class);
    }

    public List<TaskDtoV2> getAllTaskByStatusIdIn(Integer[] statusesId, String[] sortBy,String[] direction) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortBy != null && sortBy.length > 0) {
            for (int i = 0; i < sortBy.length; i++) {
                orders.add(new Sort.Order((direction[i].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC), sortBy[i]));
            }
        }
        if(statusesId.length == 0) return  listMapper.mapList(repository.findAll(Sort.by(orders)),TaskDtoV2.class);

        List<StatusV2> statuses = Arrays.stream(statusesId).map((statusId) -> statusRepository.findById(statusId).orElseThrow(() -> new NoSuchElementException("Status " + statusId + " dose not exist !!!!"))).toList();
        return listMapper.mapList(repository.findByStatusIn(statuses,Sort.by(orders)),TaskDtoV2.class);
    }



    public TaskDtoV2 updateTask(Integer id, FormTaskDtoV2 formTask){
        TasksV2 updateTask = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found"));
        updateTask.setTitle(formTask.getTitle());
        updateTask.setAssignees(formTask.getAssignees());
        updateTask.setDescription(formTask.getDescription());
        StatusV2 status = statusRepository.findById(formTask.getStatusId()).orElseThrow(() -> new ItemNotFoundException("Not found your status"));
        updateTask.setStatus(status);
      return  modelMapper.map( repository.save(updateTask),TaskDtoV2.class);
    }

    @Transactional
    public TaskDtoV2 addTask(FormTaskDtoV2 formTask){
        TasksV2 newTask = new TasksV2();
        newTask.setTitle(formTask.getTitle());
        newTask.setAssignees(formTask.getAssignees());
        newTask.setDescription(formTask.getDescription());
        StatusV2 status = statusRepository.findById(formTask.getStatusId()).orElseThrow(() -> new ItemNotFoundException("Not Found"));
        newTask.setStatus(status);
        return modelMapper.map( repository.save(newTask),TaskDtoV2.class);
    }



}
