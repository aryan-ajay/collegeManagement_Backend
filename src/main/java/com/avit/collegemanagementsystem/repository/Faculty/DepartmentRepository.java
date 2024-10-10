package com.avit.collegemanagementsystem.repository.Faculty;

import com.avit.collegemanagementsystem.model.Admin.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}

