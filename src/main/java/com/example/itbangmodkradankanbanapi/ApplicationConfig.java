package com.example.itbangmodkradankanbanapi;

import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationConfig {
@Bean
    public ModelMapper ModelMapper(){
    return  new ModelMapper();
}
    @Bean
    public ListMapper ListMapper(){
        return   ListMapper.getInstance();
    }
}
