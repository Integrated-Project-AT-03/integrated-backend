
package com.example.itbangmodkradankanbanapi.controllers.V1;

import com.example.itbangmodkradankanbanapi.services.V1.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v1/statuses")
public class StatusController {
    @Autowired
private StatusService service;
    @GetMapping("")
    public ResponseEntity<Object> getAllStatus(){
        return  ResponseEntity.ok(service.getAllStatus());
    }


}
