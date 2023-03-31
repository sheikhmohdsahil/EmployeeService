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
public class Permission  extends AuditModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer permId;
    @ColumnDefault("true")
    private Boolean isActive=true;
    @Column(nullable = false)
    private Boolean isView;
    @Column(nullable = false)
    private Boolean isAdd;

    @Column(nullable = false)
    private Boolean isUpdate;
    @Column(nullable = false)
    private Boolean isDelete;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


}
