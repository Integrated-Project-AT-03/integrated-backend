
package com.example.itbangmodkradankanbanapi.controllers;

import com.example.itbangmodkradankanbanapi.services.StatusService;
import com.example.itbangmodkradankanbanapi.services.StatusServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v2/status")
public class StatusControllerV2 {
    @Autowired
private StatusServiceV2 service;
    @GetMapping("")
    public ResponseEntity<Object> gatAllStatus(){
        return  ResponseEntity.ok(service.getAllStatus());
    }


}
