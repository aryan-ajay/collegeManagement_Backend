package com.avit.collegemanagementsystem.service.Faculty;

import com.avit.collegemanagementsystem.model.Faculty.FacultyProfile;
import com.avit.collegemanagementsystem.model.User.StudentProfile;
import com.avit.collegemanagementsystem.repository.Faculty.FacultyProfileRepository;
import com.avit.collegemanagementsystem.repository.User.StudentProfileRepository;
import com.avit.collegemanagementsystem.service.Faculty.interfaces.FacultyProfileService;
import com.avit.collegemanagementsystem.service.User.interfaces.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FacultyProfileServiceImpl implements FacultyProfileService {

    @Autowired
    private FacultyProfileRepository facultyProfileRepository;

    @Override
    public FacultyProfile createProfile(FacultyProfile profile) {
        return facultyProfileRepository.save(profile);
    }

    @Override
    public Optional<FacultyProfile> getProfileById(Long userId) {
        return facultyProfileRepository.findById(userId);
    }

    @Override
    public FacultyProfile updateProfile(Long userId, FacultyProfile profileDetails) {
        return facultyProfileRepository.findById(userId).map(profile -> {
            profile.setFullName(profileDetails.getFullName());
            profile.setEmailId(profileDetails.getEmailId());
            profile.setPhno(profileDetails.getPhno());
            // Update other fields as needed
            return facultyProfileRepository.save(profile);
        }).orElseThrow(() -> new RuntimeException("Profile not found with id " + userId));
    }

    @Override
    public void deleteProfile(Long userId) {
        facultyProfileRepository.deleteById(userId);
    }
}

