package com.example.itbangmodkradankanbanapi.services.V2;

import com.example.itbangmodkradankanbanapi.entities.V2.Setting;
import com.example.itbangmodkradankanbanapi.exceptions.ItemNotFoundException;
import com.example.itbangmodkradankanbanapi.models.Settings;
import com.example.itbangmodkradankanbanapi.repositories.V2.SettingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    @Autowired
   private SettingRepository repository;
    @Transactional
    public Setting changeSetting(String name,Integer value){
       Setting setting =  repository.findById(name).orElseThrow(()-> new ItemNotFoundException("Not found " + name +" setting"));
       setting.setValue(value);
       return repository.save(setting);
    }
    @Transactional
    public Setting setDisable(String name){
        Setting setting =  repository.findById(name).orElseThrow(()-> new ItemNotFoundException("Not found " + name +" setting"));
        setting.setEnable(false);
        return repository.save(setting);
    }
    @Transactional
    public Setting setEnable(String name){
        Setting setting =  repository.findById(name).orElseThrow(()-> new ItemNotFoundException("Not found " + name +" setting"));
        setting.setEnable(true);
        return  repository.save(setting);
    }

    public List<Setting> getAllSetting(){
        return repository.findAll();
    }
    public Setting getSetting(String name){
        return   repository.findById(name).orElseThrow(()-> new ItemNotFoundException("Not found " + name +" setting"));
    }


}


