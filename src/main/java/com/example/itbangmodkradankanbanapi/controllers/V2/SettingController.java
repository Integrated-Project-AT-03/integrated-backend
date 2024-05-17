
package com.example.itbangmodkradankanbanapi.controllers.V2;

import com.example.itbangmodkradankanbanapi.models.Settings;
import com.example.itbangmodkradankanbanapi.services.V1.StatusService;
import com.example.itbangmodkradankanbanapi.services.V2.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v2/settings")
public class SettingController {
    @Autowired
private SettingService service;
    @GetMapping("")
    public ResponseEntity<Object> getSettings(){
        return ResponseEntity.ok(service.getSetting());
    }

    @PutMapping("")
    public ResponseEntity<Object> setSettings(@RequestBody Settings newSettings){
        return ResponseEntity.ok(service.changSettings(newSettings));
    }




}
