package com.example.itbangmodkradankanbanapi.services;

import com.example.itbangmodkradankanbanapi.dtos.*;
import com.example.itbangmodkradankanbanapi.entities.Task;
import com.example.itbangmodkradankanbanapi.entities.TasksV2;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.repositories.StatusRepository;
import com.example.itbangmodkradankanbanapi.repositories.StatusRepositoryV2;
import com.example.itbangmodkradankanbanapi.repositories.TaskRepository;
import com.example.itbangmodkradankanbanapi.repositories.TaskRepositoryV2;
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

    public TaskDtoV2 deleteTask(Integer id){
      TasksV2 task =  repository.findById(id).orElseThrow(() -> new ItemNotFoundException("NOT FOUND"));
       repository.delete(task);
       return modelMapper.map(task,TaskDtoV2.class);
    }



//    public FormTaskDto updateTask(Integer id, FormTaskDto formTask){
//        Task updateTask = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found"));
//        updateTask.setTitle(formTask.getTitle());
//        updateTask.setAssignees(formTask.getAssignees());
//        updateTask.setDescription(formTask.getDescription());
//        updateTask.setStatusByIdStatus(statusRepository.findByStatusType(formTask.getStatus()));
//      return  modelMapper.map( repository.save(updateTask),FormTaskDto.class);
//    }

//    public FormTaskDto addTask(FormTaskDto formTask){
//        Task newTask = new Task();
//        newTask.setTitle(formTask.getTitle());
//        newTask.setAssignees(formTask.getAssignees());
//        newTask.setDescription(formTask.getDescription());
//        newTask.setStatusByIdStatus(statusRepository.findByStatusType(formTask.getStatus()));
//
//        return modelMapper.map( repository.save(newTask),FormTaskDto.class);
//    }


}
