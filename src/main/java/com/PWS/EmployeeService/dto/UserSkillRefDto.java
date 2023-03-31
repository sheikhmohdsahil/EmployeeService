package com.PWS.EmployeeService.dto;

import com.PWS.EmployeeService.entity.UserSkillXref;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Repository
public class UserSkillRefDto {


    private Integer id;

    private Integer userId;
    private Integer skillId;

    private UserSkillXref.Keyword proficiencyLevel;
    private Boolean isActive;

}
