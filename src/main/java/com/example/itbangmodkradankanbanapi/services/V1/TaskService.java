package com.example.itbangmodkradankanbanapi.services.V1;

import com.example.itbangmodkradankanbanapi.dtos.V1.FormTaskDto;
import com.example.itbangmodkradankanbanapi.dtos.V1.FullTaskDto;
import com.example.itbangmodkradankanbanapi.dtos.V1.TaskDto;
import com.example.itbangmodkradankanbanapi.entities.V1.Task;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.repositories.V1.StatusRepository;
import com.example.itbangmodkradankanbanapi.repositories.V1.TaskRepository;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;
    @Autowired
    private StatusRepository statusRepository;


    @Autowired
    private ListMapper listMapper;
    private final ModelMapper modelMapper = new ModelMapper();
    public FullTaskDto getTask(Integer id){

        return modelMapper.map(repository.findById(id).orElseThrow(() -> new NoSuchElementException("Task "+ id + " dose not exist !!!!")),FullTaskDto.class);
    }

    public List<TaskDto> getAllTask(){
        return listMapper.mapList(repository.findAllByOrderByCreatedOnAsc(),TaskDto.class);
    }

    public TaskDto deleteTask(Integer id){
      Task task =  repository.findById(id).orElseThrow(() -> new ItemNotFoundException("NOT FOUND"));
       repository.delete(task);
       return modelMapper.map(task,TaskDto.class);
    }



    public FormTaskDto updateTask(Integer id, FormTaskDto formTask){
        Task updateTask = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found"));
        updateTask.setTitle(formTask.getTitle());
        updateTask.setAssignees(formTask.getAssignees());
        updateTask.setDescription(formTask.getDescription());
        updateTask.setStatusByIdStatus(statusRepository.findByStatusType(formTask.getStatus()));
      return  modelMapper.map( repository.save(updateTask),FormTaskDto.class);
    }

    public FormTaskDto addTask(FormTaskDto formTask){
        Task newTask = new Task();
        newTask.setTitle(formTask.getTitle());
        newTask.setAssignees(formTask.getAssignees());
        newTask.setDescription(formTask.getDescription());
        newTask.setStatusByIdStatus(statusRepository.findByStatusType(formTask.getStatus()));

        return modelMapper.map( repository.save(newTask),FormTaskDto.class);
    }


}
