package com.avit.collegemanagementsystem.model.Faculty;

import com.avit.collegemanagementsystem.model.Role;
import com.avit.collegemanagementsystem.model.User.StudentProfile;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "faculty")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "faculty_roles",
            joinColumns = @JoinColumn(name = "faculty_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "faculty", cascade = CascadeType.ALL)
    @JsonManagedReference // This prevents infinite loop on serialization
    private FacultyProfile facultyProfile;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    // Constructor to create User object with essential parameters
    public Faculty(String username, String password, Set<Role> roles, StudentProfile studentProfile, String name, String email, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Constructor to create User object with essential parameters
    public Faculty(String username, String password, Set<Role> roles , String name, String email, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.roles = roles != null ? roles : new HashSet<>();
    }
}

