package com.PWS.EmployeeService.service;

import com.PWS.EmployeeService.Utility.CommonUtils;
import com.PWS.EmployeeService.dto.SignUpDto;
import com.PWS.EmployeeService.dto.UpdatePasswordDto;
import com.PWS.EmployeeService.dto.UserBasicDetailsDto;
import com.PWS.EmployeeService.dto.UserSkillRefDto;
import com.PWS.EmployeeService.entity.*;
import com.PWS.EmployeeService.exception.PWSException;
import com.PWS.EmployeeService.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.PWS.EmployeeService.Utility.CommonUtils.*;


@Service
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRefRepository userRoleRefRepository;
    @Autowired
    private UserSkillXref userSkillXref;
    @Autowired
    private UserSkillXrefRepository userSkillXrefRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserBasicDetailsDto userBasicDetailsDto;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserSkillRefDto userSkillRefDto;
    @Override
    public void employeeSignUp(SignUpDto signupDTO) throws Exception {

        if (!isStrongPassword(signupDTO.getPassword())) {
            throw new Exception("Password is not strong , at least one uppercase letter, one lowercase letter, one digit, and one special character needed");
        }
        Optional<User> optionalUser = userRepository.findUserByEmail(signupDTO.getEmail());
        if (optionalUser.isPresent())
            throw new Exception("User Already Exist with Email : " + signupDTO.getEmail());
        User user = new User();
        user.setDateOfBirth(signupDTO.getDob());
        user.setFirstName(signupDTO.getFirstName());
        user.setIsActive(true);
        user.setLastName(signupDTO.getLastName());
        user.setEmail(signupDTO.getEmail());
        user.setPhoneNumber(signupDTO.getPhoneNumber());
        PasswordEncoder encoder = new BCryptPasswordEncoder(8);
        // Set new password
        user.setPassword(encoder.encode(signupDTO.getPassword()));
        userRepository.save(user);

        Optional<Role> optionalRole =roleRepository.findByRoleName(signupDTO.getRoleName());
        Role role = optionalRole.get();
        UserRoleRef userRoleRef = new UserRoleRef();
        userRoleRef.setRole(role);
        userRoleRef.setUser(user);
        userRoleRef.setIsActive(true);
        userRoleRefRepository.save(userRoleRef);
    }

    @Override
    public Optional<User> fetchById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Override
        public void delete(Integer id) {userRepository.deleteById(id);}


    @Override
    public void userUpdatePassword(UpdatePasswordDto updatePasswordDto) throws Exception {

        Optional<User> optionalUser = userRepository.findUserByEmail(updatePasswordDto.getEmail());
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = null;
        if (!optionalUser.isPresent()) {
            throw new Exception("email not found");
        }
        user = optionalUser.get();
        if (encoder.matches(updatePasswordDto.getOldPassword(), user.getPassword())) {
            if (isStrongPassword(updatePasswordDto.getNewPassword())&&(updatePasswordDto.getNewPassword().equals(updatePasswordDto.getConfirmPassword()))) {
                if (!updatePasswordDto.getOldPassword().equals(updatePasswordDto.getNewPassword())) {
                    user.setPassword(encoder.encode(updatePasswordDto.getNewPassword()));
                    userRepository.save(user);
                } else {
                    throw new Exception(" old Password and new Password are same so please change the password");
                }
            } else {
                throw new Exception("Password should be strong or contains special characters");
            }
        } else {
            throw new Exception("please enter your correct old password");
        }
    }



    //skills
    @Override
    public Skill createSkills(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public void deleteSkills(Integer id) {
        skillRepository.deleteById(id);
    }

    @Override
    public List<Skill> fetchAllSkills(Skill skill) throws Exception {
        return skillRepository.findAll();
    }

    @Override
    public void addSkillsToUser(UserSkillRefDto userSkillRefDto) throws Exception {

        Optional<User> optionalUser=userRepository.findById(userSkillRefDto.getUserId());
        Optional<Skill> optionalSkill=skillRepository.findById(userSkillRefDto.getSkillId());
      //  UserSkillXref userSkillXref=null;
         UserSkillXref userSkillXref= new UserSkillXref();
        if(optionalUser.isPresent() && optionalSkill.isPresent()){
            userSkillXref.setUser(optionalUser.get());
            userSkillXref.setSkill(optionalSkill.get());
            userSkillXref.setId(userSkillRefDto.getId());
            userSkillXref.setProficiencyLevel(userSkillRefDto.getProficiencyLevel());
            userSkillXref.setIsActive(userSkillRefDto.getIsActive());
        }

        else {
            throw new PWSException("user or skills doesnot exist");



        }
        userSkillXrefRepository.save(userSkillXref);

    }

//    @Override
//    public Page<Skill> fetchAllUserSkills(int page, int pageSize, String sort, String order, Integer id)
//            throws PWSException {
//        Pageable pageable = CommonUtils.getPageable(page, pageSize, sort, order);
//        return userSkillXrefRepository.fetchAllUserSkills(pageable, id);
//    }

    public List<Skill> fetchuserSkillsByid(Integer id) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
        }
        else {
            throw new PWSException("email id "+ id + "doesnot exist" );
        }

        return userSkillXrefRepository.fetchuserSkillsByid(id);
    }



    @Override
    public UserBasicDetailsDto getInformationAfterUserLogin(String email) throws Exception {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (!optionalUser.isPresent())
            throw new PWSException("User not Exist with Email : " + email);

        User user = optionalUser.get();
        UserBasicDetailsDto userBasicDetailsDto = new UserBasicDetailsDto();
        userBasicDetailsDto.setUser(user);


        List<Role> roleList = userRoleRefRepository.findAllUserRoleByUserId(user.getId());
            userBasicDetailsDto.setRoleList(roleList);
            List<Permission> permissionList = null;
            if (roleList.size() > 0)
                permissionList = permissionRepository.getAllUserPermisonsByRoleId(roleList.get(0).getId());

            userBasicDetailsDto.setPermissionList(permissionList);
            List<Skill> skilllist = userSkillXrefRepository.fetchuserSkillsByid(user.getId());
            userBasicDetailsDto.setSkillList(skilllist);

            return userBasicDetailsDto;
        }

    @Override
    public Page<Skill> getAllSkills(Integer pageNumber, Integer pageSize) throws PWSException {
      Sort sort= Sort.by(Sort.Direction.ASC,"skillName");
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        return skillRepository.findAll(pageable);
    }


    private boolean isStrongPassword(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // check for at least one uppercase letter, one lowercase letter, one digit, and one special character
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (isSpecialChar(ch)) {
                hasSpecialChar = true;
            }
        }

        // check if password meets all criteria
        return password.length() >= 8 && hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    private boolean isSpecialChar(char ch) {
        String specialChars = "!@#$%^&*()_-+=[{]};:<>|./?";
        return specialChars.contains(Character.toString(ch));
    }

}
