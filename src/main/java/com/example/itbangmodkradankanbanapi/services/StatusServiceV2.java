package com.example.itbangmodkradankanbanapi.services;

import com.example.itbangmodkradankanbanapi.entities.Status;
import com.example.itbangmodkradankanbanapi.entities.StatusV2;
import com.example.itbangmodkradankanbanapi.repositories.StatusRepository;
import com.example.itbangmodkradankanbanapi.repositories.StatusRepositoryV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceV2 {
    @Autowired
    private StatusRepositoryV2 repository;
//    public Status getStatus(Integer id){
//        return repository.findById(id).get();
//    }
    public List<StatusV2> getAllStatus(){
        return repository.findAll();
    }
}
