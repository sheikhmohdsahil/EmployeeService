package com.PWS.EmployeeService.service;

import com.PWS.EmployeeService.dto.SignUpDto;
import com.PWS.EmployeeService.dto.UpdatePasswordDto;
import com.PWS.EmployeeService.dto.UserBasicDetailsDto;
import com.PWS.EmployeeService.dto.UserSkillRefDto;
import com.PWS.EmployeeService.entity.Skill;
import com.PWS.EmployeeService.entity.User;
import com.PWS.EmployeeService.entity.UserRoleRef;
import com.PWS.EmployeeService.entity.UserSkillXref;
import com.PWS.EmployeeService.exception.PWSException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    void employeeSignUp(SignUpDto signupDTO) throws Exception;

    Optional<User> fetchById(Integer id);

    List<User> findAll();

    void delete(Integer id);

    public void userUpdatePassword(UpdatePasswordDto updatePasswordDto) throws Exception;


    //skills
    public Skill createSkills(Skill skill);

    public void deleteSkills(Integer id);

    public List<Skill> fetchAllSkills(Skill skill) throws Exception;


    public void addSkillsToUser(UserSkillRefDto userSkillRefDto) throws Exception;

    public List<Skill> fetchuserSkillsByid(Integer id) throws Exception;


    public UserBasicDetailsDto getInformationAfterUserLogin(String email) throws Exception;

//    List<UserSkillXref> fet

//    public Page<Skill> fetchAllUserSkills(int page, int pageSize, String sort, String order, Integer id)
//            throws PWSException;

    Page<Skill> getAllSkills (Integer pageNumber,Integer pageSize)throws PWSException;
}
