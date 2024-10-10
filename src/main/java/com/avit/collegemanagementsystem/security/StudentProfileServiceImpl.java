package com.avit.collegemanagementsystem.security;

import com.avit.collegemanagementsystem.service.User.interfaces.StudentProfileService;

import com.avit.collegemanagementsystem.model.User.StudentProfile;
import com.avit.collegemanagementsystem.repository.User.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Override
    public StudentProfile createProfile(StudentProfile profile) {
        return studentProfileRepository.save(profile);
    }

    @Override
    public Optional<StudentProfile> getProfileById(Long userId) {
        return studentProfileRepository.findById(userId);
    }

    @Override
    public StudentProfile updateProfile(Long userId, StudentProfile profileDetails) {
        return studentProfileRepository.findById(userId).map(profile -> {
            profile.setFullName(profileDetails.getFullName());
            profile.setEmailId(profileDetails.getEmailId());
            profile.setPhno(profileDetails.getPhno());
            // Update other fields as needed
            return studentProfileRepository.save(profile);
        }).orElseThrow(() -> new RuntimeException("Profile not found with id " + userId));
    }

    @Override
    public void deleteProfile(Long userId) {
        studentProfileRepository.deleteById(userId);
    }
}

