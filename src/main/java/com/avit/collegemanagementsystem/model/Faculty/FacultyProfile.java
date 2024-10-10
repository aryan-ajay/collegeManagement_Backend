package com.avit.collegemanagementsystem.model.Faculty;

import com.avit.collegemanagementsystem.model.Admin.Department;
import com.avit.collegemanagementsystem.model.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "faculty_profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacultyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String photo;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    private String officeHours;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}

