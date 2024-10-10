package com.avit.collegemanagementsystem.model.Admin;

import com.avit.collegemanagementsystem.model.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "administrator_profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String photo;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}

