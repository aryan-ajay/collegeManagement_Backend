package com.avit.collegemanagementsystem.controller.User;

import com.avit.collegemanagementsystem.model.MultipleRoles;
import com.avit.collegemanagementsystem.model.Role;
import com.avit.collegemanagementsystem.model.User.StudentProfile;
import com.avit.collegemanagementsystem.model.User.User;
import com.avit.collegemanagementsystem.repository.RoleRepository;
import com.avit.collegemanagementsystem.repository.User.UserRepository;
import com.avit.collegemanagementsystem.security.jwt.JwtUtils;
import com.avit.collegemanagementsystem.security.request.LoginRequest;
import com.avit.collegemanagementsystem.security.request.SignupRequest;
import com.avit.collegemanagementsystem.security.response.MessageResponse;
import com.avit.collegemanagementsystem.security.response.UserInfoResponse;
import com.avit.collegemanagementsystem.security.services.UserDetailsImpl;
import com.avit.collegemanagementsystem.service.User.interfaces.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private StudentProfileService profileService;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail().toLowerCase())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        String username = UUID.randomUUID().toString();
        String phone = signupRequest.getPhone() == null ? "0000000000" : signupRequest.getPhone();
        User user = new User(
                username,
                encoder.encode(signupRequest.getPassword()),
                new HashSet<>(), // Roles are added below
                signupRequest.getName(),
                signupRequest.getEmail().toLowerCase(),
                phone
        );

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

        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        // Set user in StudentProfile
//        StudentProfile profile = new StudentProfile(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getPhone());
        StudentProfile profile = new StudentProfile(savedUser.getName(), savedUser.getEmail(), savedUser.getPhone());
        profile.setUser(savedUser); // Set the user in the profile

        StudentProfile savedProfile = profileService.createProfile(profile);
        savedUser.setStudentProfile(savedProfile);
        userRepository.save(savedUser);

        return ResponseEntity.ok(new MessageResponse("User created successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;

//        try {
//            authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//        }catch (AuthenticationException exception) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("message","Bad credentials");
//            map.put("status", false);
//
//            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
//        }

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

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        System.out.println("Authentication roles output after signin in filter: " + roles);

        StudentProfile userProfile = dbUser.getStudentProfile();

//        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), roles, "Login Successful", dbUser.getInternshipApplied());
        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), roles, "Login Successful", userProfile);

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).body(response);

//        Below code is for JWT Cookies based authentication

//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),userDetails.getUsername(), roles,"Login Successful");
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);
    }

}
