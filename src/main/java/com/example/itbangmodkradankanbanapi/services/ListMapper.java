package com.example.itbangmodkradankanbanapi.services;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListMapper {
    private static final ListMapper listMapper = new ListMapper();
    private static final ModelMapper modelMapper = new ModelMapper();

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source.stream().map(entity -> modelMapper.map(entity, targetClass)).collect(Collectors.toList());
    }



}
