package com.PWS.EmployeeService.entity;


import com.PWS.EmployeeService.Utility.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")

public class Role extends AuditModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Boolean isActive;


}
