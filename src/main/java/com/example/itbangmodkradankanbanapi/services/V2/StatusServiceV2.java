package com.example.itbangmodkradankanbanapi.services.V2;


import com.example.itbangmodkradankanbanapi.dtos.V2.StatusDtoV2;
import com.example.itbangmodkradankanbanapi.entities.V2.StatusV2;
import com.example.itbangmodkradankanbanapi.exceptions.ForeignKeyException;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.repositories.V2.StatusRepositoryV2;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StatusServiceV2 {
    @Autowired
    private StatusRepositoryV2 repository;
    @Autowired
    private ListMapper listMapper;
    private final ModelMapper modelMapper = new ModelMapper();
        public StatusDtoV2 getStatus(Integer id){
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new NoSuchElementException("Status "+ id + " dose not exist !!!!")), StatusDtoV2.class) ;
    }


    public List<StatusDtoV2> getAllStatus() {
        return listMapper.mapList(repository.findAll(),StatusDtoV2.class);
    }
    @Transactional
    public StatusDtoV2 updateStatus(Integer id, StatusDtoV2 status) {
        try {
        StatusV2 updatedStatus = repository.findById(id).orElseThrow(() -> new NoSuchElementException("NOT FOUND"));
        updatedStatus.setStatusName(status.getStatusName());
        updatedStatus.setStatusDescription(status.getStatusDescription());
//        updatedStatus.setColorHex(status.getHex());
        return modelMapper.map(repository.save(updatedStatus), StatusDtoV2.class) ;
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("could not execute statement [Duplicate entry "+ status.getStatusName() + " for key 'statuses.name_UNIQUE'] (description,name) value (?,?)]; constraint [statuses.name_UNIQUE]");
        }
    }

    @Transactional
    public StatusDtoV2 addStatus(StatusDtoV2 status) {
            try {
                StatusV2 newStatus =  repository.save(modelMapper.map(status,StatusV2.class));
                return  modelMapper.map(newStatus,StatusDtoV2.class);
            } catch (DataIntegrityViolationException e){
                throw new DataIntegrityViolationException("could not execute statement [Duplicate entry "+ status.getStatusName() + " for key 'statuses.name_UNIQUE'] (description,name) value (?,?)]; constraint [statuses.name_UNIQUE]");
            }


    }
    @Transactional

    public StatusDtoV2 deleteStatus(Integer id){
        try {
    StatusV2 task = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("NOT FOUND"));
    repository.delete(task);
    return modelMapper.map(task,StatusDtoV2.class);
}catch (DataIntegrityViolationException e){
    throw new ForeignKeyException("could not execute statement [Cannot delete or update a parent row: a foreign key constraint fails (`karban`.`tasksV2`, CONSTRAINT `fk_Tasks_statusV2` FOREIGN KEY (`id_status`) REFERENCES `statusV2` (`id_status`))] [delete from statusV2 where id_status=?]; SQL [delete from statusV2 where id_status=?]; constraint [null]");
}
    }
}
