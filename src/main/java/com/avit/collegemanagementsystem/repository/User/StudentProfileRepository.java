package com.avit.collegemanagementsystem.repository.User;

import com.avit.collegemanagementsystem.model.User.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
}

