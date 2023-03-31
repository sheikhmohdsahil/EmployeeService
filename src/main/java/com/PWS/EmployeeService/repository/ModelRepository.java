package com.PWS.EmployeeService.repository;


import com.PWS.EmployeeService.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model,Integer> {
}
