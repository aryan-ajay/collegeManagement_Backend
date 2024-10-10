package com.avit.collegemanagementsystem.service.User.interfaces;

import com.avit.collegemanagementsystem.model.User.StudentProfile;

import java.util.Optional;

public interface StudentProfileService {
    StudentProfile createProfile(StudentProfile profile);

    Optional<StudentProfile> getProfileById(Long userId);

    StudentProfile updateProfile(Long userId, StudentProfile profileDetails);

    void deleteProfile(Long userId);
}
