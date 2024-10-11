package com.avit.collegemanagementsystem.security.response;

import com.avit.collegemanagementsystem.model.Faculty.FacultyProfile;
import com.avit.collegemanagementsystem.model.User.StudentProfile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private Long  id;
    private String username;
    private String message;
    private List<String> roles;
    private boolean internshipApplied;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private StudentProfile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FacultyProfile facultyProfile;

    public UserInfoResponse(Long  id, String username, List<String> roles, String jwtToken, String message) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.message = message;
    }

    public UserInfoResponse(Long  id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public UserInfoResponse(Long id, List<String> roles, String loginSuccessful, StudentProfile profile) {
        this.id = id;
        this.roles = roles;
        this.message = loginSuccessful;
        this.profile = profile;
    }

    public UserInfoResponse(Long id, List<String> roles, String loginSuccessful, FacultyProfile profile) {
        this.id = id;
        this.roles = roles;
        this.message = loginSuccessful;
        this.facultyProfile = profile;
    }
}
