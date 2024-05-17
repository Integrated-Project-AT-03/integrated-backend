package com.example.itbangmodkradankanbanapi.services.V2;

import com.example.itbangmodkradankanbanapi.dtos.V2.ColorDto;
import com.example.itbangmodkradankanbanapi.entities.V2.Setting;
import com.example.itbangmodkradankanbanapi.models.Settings;
import com.example.itbangmodkradankanbanapi.repositories.V2.ColorRepository;
import com.example.itbangmodkradankanbanapi.repositories.V2.SettingRepository;
import com.example.itbangmodkradankanbanapi.utils.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SettingService {
   private Settings settings  = new Settings();
    public Settings changSettings(Settings newSettings){
        settings.setNumOfLimitsTask(newSettings.getNumOfLimitsTask());
        return settings;
    }

    public Integer getNumberOfLimitsTasks(){
       return settings.getNumOfLimitsTask();
    }

    public Settings getSetting(){
       return settings;
    }


}


//@Autowired
//private SettingRepository setting;
//public Settings changSettings(Setting[] newSettings){
//    return  setting.saveAll()
//
//}
//
//public Integer getNumberOfLimitsTasks(){
//    return settings.getNumOfLimitsTask();
//}
//
//public Settings getSetting(){
//    return settings;
//}
