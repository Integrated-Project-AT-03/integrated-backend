package com.example.itbangmodkradankanbanapi.services;

import com.example.itbangmodkradankanbanapi.dtos.*;
import com.example.itbangmodkradankanbanapi.entities.StatusV2;
import com.example.itbangmodkradankanbanapi.entities.Task;
import com.example.itbangmodkradankanbanapi.entities.TasksV2;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.repositories.StatusRepository;
import com.example.itbangmodkradankanbanapi.repositories.StatusRepositoryV2;
import com.example.itbangmodkradankanbanapi.repositories.TaskRepository;
import com.example.itbangmodkradankanbanapi.repositories.TaskRepositoryV2;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



    public FormTaskDtoV2 updateTask(Integer id, FormTaskDtoV2 formTask){
        TasksV2 updateTask = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found"));
        updateTask.setTitle(formTask.getTitle());
        updateTask.setAssignees(formTask.getAssignees());
        updateTask.setDescription(formTask.getDescription());
        StatusV2 status = statusRepository.findByStatusName(formTask.getStatusName());
        updateTask.setStatus(status);
      return  modelMapper.map( repository.save(updateTask),FormTaskDtoV2.class);
    }
    @Transactional
    public FormTaskDtoV2 addTask(FormTaskDtoV2 formTask){
        TasksV2 newTask = new TasksV2();
        newTask.setTitle(formTask.getTitle());
        newTask.setAssignees(formTask.getAssignees());
        newTask.setDescription(formTask.getDescription());
        System.out.println(formTask);
        StatusV2 status = statusRepository.findByStatusName(formTask.getStatusName());
        newTask.setStatus(status);
        return modelMapper.map( repository.save(newTask),FormTaskDtoV2.class);
    }
    @Transactional
    public List<TaskDtoV2> ChangeTasksByStatus(Integer oldIdStatus,Integer newIdStatus){
        List<TasksV2> targetTasks = repository.findAllByStatus(statusRepository.findById(oldIdStatus).get());
        StatusV2 updateStatus = statusRepository.findById(newIdStatus).get();
       List<TasksV2> updatedTasks = targetTasks.stream().map((task)-> {
           task.setStatus(updateStatus);
           return task;
        }).toList();

       return listMapper.mapList(repository.saveAll(updatedTasks),TaskDtoV2.class);
    }


}
