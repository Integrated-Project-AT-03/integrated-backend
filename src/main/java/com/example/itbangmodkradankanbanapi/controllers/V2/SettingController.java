
package com.example.itbangmodkradankanbanapi.controllers.V2;

import com.example.itbangmodkradankanbanapi.services.V2.SettingService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<Object> getAllSetting() {
        return ResponseEntity.ok(service.getAllSetting());
    }

    @GetMapping("{name}")
    public ResponseEntity<Object> getSetting(@PathVariable String name) {
        return ResponseEntity.ok(service.getSetting(name));
    }

    @PutMapping("{name}/{value}/{statusSetting}")
    public ResponseEntity<Object> setValue(@PathVariable @NotNull String name, @PathVariable @NotNull @Min(10) @Max(30) Integer value, @PathVariable @NotNull String statusSetting) {
        return ResponseEntity.ok(service.changeSetting(name, value, statusSetting));
    }

    @PutMapping("{name}/{value}")
    public ResponseEntity<Object> setValue(@PathVariable @NotNull String name, @PathVariable @NotNull Integer value) {
        return ResponseEntity.ok(service.changeSetting(name, value, ""));
    }
}