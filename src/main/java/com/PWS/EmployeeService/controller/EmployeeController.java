package com.PWS.EmployeeService.controller;

import com.PWS.EmployeeService.Utility.ApiSuccess;
import com.PWS.EmployeeService.Utility.CommonUtils;
import com.PWS.EmployeeService.dto.*;
import com.PWS.EmployeeService.entity.Skill;
import com.PWS.EmployeeService.entity.User;
import com.PWS.EmployeeService.entity.UserSkillXref;
import com.PWS.EmployeeService.exception.PWSException;
import com.PWS.EmployeeService.jwtconfig.JwtUtil;
import com.PWS.EmployeeService.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
   Logger log = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
   private EmployeeService employeeService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Operation(summary = "Authenticating the User")
    @PostMapping("/authenticate")
    public String generateToken(@RequestBody LoginDto loginDto) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return new String("token:" + jwtUtil.generateToken(loginDto.getEmail()));
    }

    @PostMapping("/public/employeeSignUp")
    public ResponseEntity<Object> employeeSignUp(@RequestBody SignUpDto signUpDto) throws Exception {

    employeeService.employeeSignUp(signUpDto);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }



    @DeleteMapping("/private/logout/token")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if(jwtUtil.isTokenBlacklisted(jwt))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalidated.");
            String userDetails = jwtUtil.getUsernameFromToken(jwt);
            // Invalidate the token
            jwtUtil.invalidateToken(jwt);
            // Clear user details from session
            request.getSession().removeAttribute("userDetails");
            return ResponseEntity.ok("Successfully logged out.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
    }
    @GetMapping("/private/fetchByEmployeeId/{id}")
    public ResponseEntity<Object> fetchEmployeeById(@PathVariable Integer id){

        Optional<User> user= employeeService.fetchById(id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK,user));

    }

    @GetMapping("/private/alluser")
    public List<User> getAll() {
         employeeService.findAll();
        return (List<User>) CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));

    }


    @DeleteMapping("/private/deleteUser/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Integer id) {
        employeeService.delete(id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @Operation(summary = "Updating User Password")
   // @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User's Password Updated  Successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_SAVED_200_SUCCESSFULL)})}),@ApiResponse(responseCode = "400", description = "Invalid password", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_SAVE_400_FAILURE)})})})
    @PutMapping("/private/user/password")
    public ResponseEntity<Object> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto)throws  Exception{
        employeeService.userUpdatePassword(updatePasswordDto);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }



    //skills

    @PostMapping("/private/addSkills")
    public ResponseEntity<Object> addSkills(@RequestBody Skill skill) throws Exception{
        employeeService.createSkills(skill);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @DeleteMapping("/private/deleteSkills/{id}")
    public ResponseEntity<Object> deleteSkills(@PathVariable Integer id){
        employeeService.deleteSkills(id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }



    @PostMapping("/private/addSkillsToUser")
    public ResponseEntity<Object> addSkillsToUser(@RequestBody UserSkillRefDto userSkillRefDto)throws Exception{
        employeeService.addSkillsToUser(userSkillRefDto);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }


    @GetMapping("/private/user-login-information")
    public ResponseEntity<Object> getUserInformation(@RequestParam String email)throws Exception{
        UserBasicDetailsDto userBasicDetailsDto=employeeService.getInformationAfterUserLogin(email);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, userBasicDetailsDto));

    }


    @GetMapping("/private/user-skills-by-id/{id}")
    public ResponseEntity<Object> fetchUserSkillsByid(@PathVariable Integer id) throws Exception {
        List<Skill> listOfActiveUserSkillXref=  employeeService.fetchuserSkillsByid(id);

        return  CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK,listOfActiveUserSkillXref));

    }

    @GetMapping("/private/allskills")
    public ResponseEntity<Object> findAllSkills(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", required = false, defaultValue ="1") Integer pageSize
            )throws PWSException{
        Page<Skill> skilllist = employeeService.getAllSkills(pageNumber,pageSize);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, skilllist));

    }



}
