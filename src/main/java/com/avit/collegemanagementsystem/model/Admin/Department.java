package com.avit.collegemanagementsystem.model.Admin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "department")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    public Department(String cse, String computerScienceAndEngineering) {
        this.name = cse;
        this.description = computerScienceAndEngineering;
    }
}

