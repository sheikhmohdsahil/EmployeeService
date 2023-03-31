package com.PWS.EmployeeService.entity;
import com.PWS.EmployeeService.Utility.AuditModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User extends AuditModel implements UserDetails, Serializable {

    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String firstName;

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = false)
    private String lastName;
    @JsonFormat(pattern = "yyyy/MM/dd")
    @Column(nullable = false)
    private Date dateOfBirth;
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private long phoneNumber;

    @Column(nullable = false)
    // @ValidPassword
    private String password;

    @Column(name = "reset_token")
    private String resetToken;
    private Boolean isActive;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }


    public void setResetPasswordToken(Object o) {
    }
}
