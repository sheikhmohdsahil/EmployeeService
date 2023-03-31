package com.PWS.EmployeeService.dto;

import com.PWS.EmployeeService.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Repository
public class UserBasicDetailsDto {

    private User user;

    private List<Role> roleList;

    private List<Permission> permissionList;

    private List<Skill> skillList;
}
