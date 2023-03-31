package com.PWS.EmployeeService.repository;

import com.PWS.EmployeeService.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Integer> {

    @Query("select o from Permission o where o.role.id= :roleId")
    List<Permission> getAllUserPermisonsByRoleId(Integer roleId);
}
