package com.example.itbangmodkradankanbanapi.services;

import com.example.itbangmodkradankanbanapi.dtos.FullTaskDto;
import com.example.itbangmodkradankanbanapi.dtos.TaskDto;
import com.example.itbangmodkradankanbanapi.entities.Task;
import com.example.itbangmodkradankanbanapi.exceptions.ErrorResponse;
import com.example.itbangmodkradankanbanapi.repositories.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    @Autowired
    private ListMapper listMapper;

    public FullTaskDto getTask(Integer id){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new NoSuchElementException("Task "+ id + " dose not exist !!!!")),FullTaskDto.class);
    }

    public List<TaskDto> getAllTask(){
        return listMapper.mapList(repository.findAllByOrderByCreatedOnAsc(),TaskDto.class);
    }

    public void deleteTask(Integer id){
       repository.delete(repository.findById(id).orElseThrow(() -> new NoSuchElementException("Task "+ id + " dose not exist !!!!")));
    }

    public Task updateTask(Integer id, Task newTask){
        Task task = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Task "+ id + " dose not exist !!!!"));
        task.setTitle(newTask.getTitle());
        task.setDescription(newTask.getDescription());
        task.setStatusByIdStatus(newTask.getStatusByIdStatus());
      return  repository.save(task);
    }

    public Task addTask(Task task){
        return repository.save(task);
    }


}
