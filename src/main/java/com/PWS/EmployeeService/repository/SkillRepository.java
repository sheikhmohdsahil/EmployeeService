package com.PWS.EmployeeService.repository;

import com.PWS.EmployeeService.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Integer> {

}