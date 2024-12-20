package com.example.itbangmodkradankanbanapi.services.V2;

import com.example.itbangmodkradankanbanapi.dtos.V2.ColorDto;

import com.example.itbangmodkradankanbanapi.repositories.V2.ColorRepository;

import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ColorService {
    @Autowired
    private ColorRepository repository;
    @Autowired
    ListMapper listMapper;
    private final ModelMapper modelMapper = new ModelMapper();
    public ColorDto getColor(Integer id){
        return  modelMapper.map(repository.findById(id).orElseThrow(() -> new NoSuchElementException("Color "+ id + " dose not exist !!!!")),ColorDto.class);
    }
    public List<ColorDto> getAllColor(){
        return listMapper.mapList(repository.findAll(),ColorDto.class);
    }
}
