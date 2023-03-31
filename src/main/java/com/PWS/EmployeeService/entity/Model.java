package com.PWS.EmployeeService.entity;

import com.PWS.EmployeeService.Utility.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

//import javax.persistence.*
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Model extends AuditModel implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    @Column(name = "model_id")
    private Integer id;
    @Column(nullable = false)
    private String name;

    @ColumnDefault("true")
    private Boolean isActive=true;

}




