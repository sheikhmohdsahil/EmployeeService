package com.PWS.EmployeeService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto {

    private Integer id;

    private Boolean isActive;

    private Boolean isView;

    private Boolean isAdd;

    private Boolean isUpdate;

    private Boolean isDelete;

    private Integer modelId;

    private Integer roleId;

}
