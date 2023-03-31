package com.PWS.EmployeeService.entity;



import com.PWS.EmployeeService.Utility.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import java.io.Serializable;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserRoleRef extends AuditModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "role_Id")
    private Role role;

    @ColumnDefault("TRUE")
    private Boolean isActive;



}
