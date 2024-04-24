package com.example.itbangmodkradankanbanapi.services;

import com.example.itbangmodkradankanbanapi.dtos.FullTaskDto;
import com.example.itbangmodkradankanbanapi.dtos.TaskDto;
import com.example.itbangmodkradankanbanapi.entities.Task;
import com.example.itbangmodkradankanbanapi.repositories.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    @Autowired
    private ListMapper listMapper;

    public FullTaskDto getTask(String id){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(repository.findById(id).get(),FullTaskDto.class);
    }

    public List<TaskDto> getAllTask(){
        return listMapper.mapList(repository.findAll(),TaskDto.class);
    }

    public void deleteTask(String id){
       repository.delete(repository.findById(id).get());
    }

    public Task updateTask(String id, Task newTask){
        Task task = repository.findById(id).get();
        task.setTitle(newTask.getTitle());
        task.setDescription(newTask.getDescription());
        task.setStatusByIdStatus(newTask.getStatusByIdStatus());
      return  repository.save(task);
    }

    public Task addTask(Task task){
        return repository.save(task);
    }
}
