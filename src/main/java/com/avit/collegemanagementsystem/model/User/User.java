package com.avit.collegemanagementsystem.model.User;

import com.avit.collegemanagementsystem.model.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference // This prevents infinite loop on serialization
    private StudentProfile studentProfile;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    // Constructor to create User object with essential parameters
    public User(String username, String password, Set<Role> roles, StudentProfile studentProfile, String name, String email, String phone) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.studentProfile = studentProfile;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Constructor to create User object with essential parameters
    public User(String username, String password, Set<Role> roles, String name, String email, String phone) {
        this.username = username;
        this.password = password;
        this.roles = roles != null ? roles : new HashSet<>(); // If roles are not passed, default to an empty set
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
