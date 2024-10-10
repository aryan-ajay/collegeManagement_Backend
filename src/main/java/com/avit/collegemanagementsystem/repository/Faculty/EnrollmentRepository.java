package com.avit.collegemanagementsystem.repository.Faculty;

import com.avit.collegemanagementsystem.model.Faculty.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}

