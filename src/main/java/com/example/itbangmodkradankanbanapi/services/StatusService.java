package com.example.itbangmodkradankanbanapi.services;

import com.example.itbangmodkradankanbanapi.dtos.FullTaskDto;
import com.example.itbangmodkradankanbanapi.dtos.TaskDto;
import com.example.itbangmodkradankanbanapi.entities.Status;
import com.example.itbangmodkradankanbanapi.entities.Task;
import com.example.itbangmodkradankanbanapi.repositories.StatusRepository;
import com.example.itbangmodkradankanbanapi.repositories.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    @Autowired
    private StatusRepository repository;


    public Status getStatus(String id){
        return repository.findById(id).get();
    }

    public List<Status> getAllStatus(){
        return repository.findAll();
    }

}
