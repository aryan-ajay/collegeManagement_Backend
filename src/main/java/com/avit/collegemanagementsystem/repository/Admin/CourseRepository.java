package com.avit.collegemanagementsystem.repository.Admin;

import com.avit.collegemanagementsystem.model.Admin.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

