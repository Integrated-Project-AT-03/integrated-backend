package com.example.itbangmodkradankanbanapi.controllers;

import com.example.itbangmodkradankanbanapi.entities.Employee;
import com.example.itbangmodkradankanbanapi.services.EmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
private EmployeeService service;

    @GetMapping("{id}")
    public Employee findEmployee(@PathVariable Integer id){
        return service.getEmployee(id);
    }

    @PostMapping("")
    @Transactional
    public Employee addEmployee(@RequestBody Employee employee){
        return service.addEmployee(employee);
    }
}
