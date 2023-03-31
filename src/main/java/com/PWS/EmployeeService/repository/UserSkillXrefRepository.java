package com.PWS.EmployeeService.repository;

import com.PWS.EmployeeService.entity.Skill;
import com.PWS.EmployeeService.entity.UserSkillXref;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSkillXrefRepository extends JpaRepository<UserSkillXref, Integer> {

    @Query("select o.skill from UserSkillXref o where o.user.id=:id")
    List<Skill> fetchuserSkillsByid(Integer id);


    @Query("select o.skill from UserSkillXref o where o.user.id=:id")
    Page<Skill> fetchAllUserSkills(Pageable pageable, Integer id);

//    @Query("select o.user,o.skill from UserSkillXref o where o.user.id=:id")
//    List<UserSkillXref> fetchallSkillsofUserByid(Integer id);

}
