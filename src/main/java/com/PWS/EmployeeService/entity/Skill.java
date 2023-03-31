package com.PWS.EmployeeService.entity;

import com.PWS.EmployeeService.Utility.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "skill_master_data")
public class Skill extends AuditModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "skill_name", nullable = false, unique = true)
    private String skillName;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("TRUE")
    private Boolean isActive;

}