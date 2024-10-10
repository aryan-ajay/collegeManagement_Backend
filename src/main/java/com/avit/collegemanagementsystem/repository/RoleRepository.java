package com.avit.collegemanagementsystem.repository;

import com.avit.collegemanagementsystem.model.AppRole;
import com.avit.collegemanagementsystem.model.MultipleRoles;
import com.avit.collegemanagementsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(MultipleRoles appRole);
}
