package com.example.itbangmodkradankanbanapi.services.V1;
import com.example.itbangmodkradankanbanapi.entities.V1.Status;
import com.example.itbangmodkradankanbanapi.repositories.V1.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    @Autowired
    private StatusRepository repository;

    public List<Status> getAllStatus(){
        return repository.findAll();
    }
}
