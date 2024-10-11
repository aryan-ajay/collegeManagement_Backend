package com.avit.collegemanagementsystem.model.Faculty;

import com.avit.collegemanagementsystem.model.Admin.Department;
import com.avit.collegemanagementsystem.model.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @OneToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    @JsonBackReference // This prevents infinite loop by ignoring this reference
    private Faculty faculty;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    private String officeHours;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email_id", unique = true, nullable = false)
    private String emailId;

    @Column(name = "phno", nullable = false)
    private String phno;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "college")
    private String college;

    @Column(name = "summary")
    private String summary;

    public FacultyProfile(String name, String email, String phone) {
        this.fullName = name;
        this.emailId = email;
        this.phno = phone;
    }
}

