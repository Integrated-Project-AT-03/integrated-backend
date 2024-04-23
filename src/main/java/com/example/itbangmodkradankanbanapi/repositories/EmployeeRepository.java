package com.example.itbangmodkradankanbanapi.repositories;

import com.example.itbangmodkradankanbanapi.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
}
