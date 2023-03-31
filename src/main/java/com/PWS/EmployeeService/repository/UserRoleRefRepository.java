package com.PWS.EmployeeService.repository;

import com.PWS.EmployeeService.entity.Role;
import com.PWS.EmployeeService.entity.UserRoleRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRefRepository extends JpaRepository<UserRoleRef,Integer> {
//    @Query("select o.role from UserRoleXref o where o.user.id=:id and o.role.isActive=True")
//    List<Role> finfAllUserRolesById(Integer id);
    @Query("select o.role from UserRoleRef o where o.user.id=:id and o.role.isActive=true")
    List<Role> findAllUserRoleByUserId(Integer id);


}
