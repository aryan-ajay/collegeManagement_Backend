package com.avit.collegemanagementsystem.controller;

import com.avit.collegemanagementsystem.model.Admin.Department;
import com.avit.collegemanagementsystem.model.Faculty.Faculty;
import com.avit.collegemanagementsystem.model.Faculty.FacultyProfile;
import com.avit.collegemanagementsystem.model.MultipleRoles;
import com.avit.collegemanagementsystem.model.Role;
import com.avit.collegemanagementsystem.model.User.StudentProfile;
import com.avit.collegemanagementsystem.model.User.User;
import com.avit.collegemanagementsystem.repository.Faculty.FacultyRepository;
import com.avit.collegemanagementsystem.repository.RoleRepository;
import com.avit.collegemanagementsystem.repository.User.UserRepository;
import com.avit.collegemanagementsystem.security.jwt.JwtUtils;
import com.avit.collegemanagementsystem.security.request.LoginRequest;
import com.avit.collegemanagementsystem.security.request.SignupRequest;
import com.avit.collegemanagementsystem.security.response.MessageResponse;
import com.avit.collegemanagementsystem.security.response.UserInfoResponse;
import com.avit.collegemanagementsystem.security.services.FacultyDetailsImpl;
import com.avit.collegemanagementsystem.security.services.UserDetailsImpl;
import com.avit.collegemanagementsystem.service.Faculty.interfaces.FacultyProfileService;
import com.avit.collegemanagementsystem.service.User.interfaces.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentProfileService profileService;

    @Autowired
    private FacultyProfileService facultyProfileService;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {

        String username = UUID.randomUUID().toString();
        String phone = signupRequest.getPhone() == null ? "0000000000" : signupRequest.getPhone();

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(MultipleRoles.STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "student":
                        Role adminRole = roleRepository.findByRoleName(MultipleRoles.STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "faculty":
                    Role facultyRole = roleRepository.findByRoleName(MultipleRoles.FACULTY_MEMBER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(facultyRole);
                    break;
                    default:
                        Role userRole = roleRepository.findByRoleName(MultipleRoles.ADMINISTRATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        if (roles.size() == 1) {

            Role selectedRole = roles.iterator().next(); // Get the first (and only) role

            if (selectedRole.getRoleName().equals(MultipleRoles.STUDENT)) {

                if (userRepository.existsByUsername(signupRequest.getUsername())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
                }

                if (userRepository.existsByEmail(signupRequest.getEmail().toLowerCase())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
                }

                User user = new User(username, encoder.encode(signupRequest.getPassword()), roles, signupRequest.getName(), signupRequest.getEmail().toLowerCase(), phone);
                StudentProfile profile = new StudentProfile(user.getName(), user.getEmail(), user.getPhone());
                profile.setUser(user);
                user.setStudentProfile(profile);
                userRepository.save(user);
                profileService.createProfile(profile);
                return ResponseEntity.ok(new MessageResponse("User created successfully"));
            } else if (selectedRole.getRoleName().equals(MultipleRoles.FACULTY_MEMBER)) {

                if (facultyRepository.existsByUsername(signupRequest.getUsername())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
                }

                if (facultyRepository.existsByEmail(signupRequest.getEmail().toLowerCase())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
                }

                Faculty faculty = new Faculty(username, encoder.encode(signupRequest.getPassword()), roles, signupRequest.getName(), signupRequest.getEmail().toLowerCase(), phone);

                FacultyProfile profile = new FacultyProfile(faculty.getName(), faculty.getEmail(), faculty.getPhone());

                Department department = new Department("CSE","Computer Science and Engineering");
                profile.setFaculty(faculty);
                profile.setDepartment(department);
                faculty.setFacultyProfile(profile);
                facultyRepository.save(faculty);
                facultyProfileService.createProfile(profile);
                return ResponseEntity.ok(new MessageResponse("Faculty created successfully"));
            } else {
//                return ResponseEntity.ok(new MessageResponse("User created successfully"));
            }
        }

        return ResponseEntity.ok(new MessageResponse("No Profile created"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;

        Set<String> strRoles = loginRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(MultipleRoles.STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "student":
                        Role adminRole = roleRepository.findByRoleName(MultipleRoles.STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "faculty":
                        Role facultyRole = roleRepository.findByRoleName(MultipleRoles.FACULTY_MEMBER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(facultyRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(MultipleRoles.ADMINISTRATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        if (roles.size() == 1) {

            Role selectedRole = roles.iterator().next(); // Get the first (and only) role

            if (selectedRole.getRoleName().equals(MultipleRoles.STUDENT)) {
                User dbUser = userRepository.findByEmail(loginRequest.getEmail().toLowerCase());

                if(dbUser == null) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Please Check Your Email Id"));
                }

                try {
                    authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(dbUser.getUsername(), loginRequest.getPassword()));
                } catch (AuthenticationException exception) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("message", "Please Enter Correct Password");
                    map.put("status", false);

                    return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
                }

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Authentication output after signin in filter: " + authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                System.out.println("Authentication userDetails output after signin in filter: " + userDetails);
                String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

                List<String> authRoles = userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList());

                System.out.println("Authentication roles output after signin in filter: " + authRoles);

                StudentProfile userProfile = dbUser.getStudentProfile();

                // UserInfoResponse response = new UserInfoResponse(userDetails.getId(), roles, "Login Successful", dbUser.getInternshipApplied());
                UserInfoResponse response = new UserInfoResponse(userDetails.getId(), authRoles, "Login Successful", userProfile);

                return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).body(response);

            } else if (selectedRole.getRoleName().equals(MultipleRoles.FACULTY_MEMBER)) {
                Faculty dbFaculty = facultyRepository.findByEmail(loginRequest.getEmail().toLowerCase());

                if(dbFaculty == null) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Please Check Your Email Id"));
                }

                System.out.println("dbfaculty username: " + dbFaculty.getUsername() + "dbfaculty password: " + dbFaculty.getPassword());
                System.out.println("dbfaculty username: " + dbFaculty.getUsername() + "request password: " + loginRequest.getPassword());

                try {
                    authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(dbFaculty.getUsername(), loginRequest.getPassword()));
                } catch (AuthenticationException exception) {
                    System.out.println("AuthenticationException output during signin: " + exception);
                    Map<String, Object> map = new HashMap<>();
                    map.put("message", "Please Enter Correct Password");
                    map.put("status", false);

                    return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
                }

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Authentication output after signin in filter: " + authentication);

                FacultyDetailsImpl userDetails = (FacultyDetailsImpl) authentication.getPrincipal();
                System.out.println("Authentication userDetails output after signin in filter: " + userDetails);
                String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

                List<String> authRoles = userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList());

                System.out.println("Authentication roles output after signin in filter: " + authRoles);

                FacultyProfile facultyProfile = dbFaculty.getFacultyProfile();

                // UserInfoResponse response = new UserInfoResponse(userDetails.getId(), roles, "Login Successful", dbUser.getInternshipApplied());
                UserInfoResponse response = new UserInfoResponse(userDetails.getId(), authRoles, "Login Successful", facultyProfile);

                return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).body(response);
            }
        }

//        Below code is for JWT Cookies based authentication

//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),userDetails.getUsername(), roles,"Login Successful");
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);

        return new ResponseEntity("Not able to signi ", HttpStatus.NOT_FOUND);
    }

}
