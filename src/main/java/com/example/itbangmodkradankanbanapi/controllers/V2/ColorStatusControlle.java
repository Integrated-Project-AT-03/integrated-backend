
package com.example.itbangmodkradankanbanapi.controllers.V2;
import com.example.itbangmodkradankanbanapi.services.V2.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v2/colors")
public class ColorStatusControlle {
    @Autowired
private ColorService service;
    @GetMapping("{id}")
    public ResponseEntity<Object> findStatus(@PathVariable Integer id){
        return ResponseEntity.ok(service.getColor(id));
    }
    @GetMapping("")
    public ResponseEntity<Object> getAllStatus(){
        return  ResponseEntity.ok(service.getAllColor());
    }
}
