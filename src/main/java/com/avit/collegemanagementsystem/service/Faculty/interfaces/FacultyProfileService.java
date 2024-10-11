package com.avit.collegemanagementsystem.service.Faculty.interfaces;

import com.avit.collegemanagementsystem.model.Faculty.FacultyProfile;
import com.avit.collegemanagementsystem.model.User.StudentProfile;

import java.util.Optional;

public interface FacultyProfileService {
    FacultyProfile createProfile(FacultyProfile profile);

    Optional<FacultyProfile> getProfileById(Long userId);

    FacultyProfile updateProfile(Long userId, FacultyProfile profileDetails);

    void deleteProfile(Long userId);
}
