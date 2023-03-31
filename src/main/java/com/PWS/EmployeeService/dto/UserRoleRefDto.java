package com.PWS.EmployeeService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRefDto {

    private Integer id;

    private Integer userId;

    private Integer roleId;

    private Boolean isActive;
}
