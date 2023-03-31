package com.PWS.EmployeeService.repository;

import com.PWS.EmployeeService.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    @Query("select o from Role o where o.name=:roleName")
    Optional<Role> findByRoleName(String roleName);

}
