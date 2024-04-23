package com.example.itbangmodkradankanbanapi.services;

import com.example.itbangmodkradankanbanapi.entities.Employee;
import com.example.itbangmodkradankanbanapi.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repository;


    public Employee getEmployee(Integer id){
        return repository.findById(id).get();
    }

    public Employee addEmployee(Employee employee){
         Employee newEmployee = new Employee();
         newEmployee.setEmpId(employee.getEmpId());
         newEmployee.setEmpName(employee.getEmpName());
        return repository.save(newEmployee);
    }
}
