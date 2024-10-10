package com.avit.collegemanagementsystem.model.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // This prevents infinite loop by ignoring this reference
    private User user;

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

    @Column(name = "course")
    private String course;

    @Column(name = "summary")
    private String summary;

//    public StudentProfile(Long id, String name, String email, String phone) {
//        this.id = id;
//        this.fullName = name;
//        this.emailId = email;
//        this.phno = phone;
//    }

    public StudentProfile(String name, String email, String phone) {
        this.fullName = name;
        this.emailId = email;
        this.phno = phone;
    }
}
