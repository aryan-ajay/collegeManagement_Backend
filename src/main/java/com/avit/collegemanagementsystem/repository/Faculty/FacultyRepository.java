package com.avit.collegemanagementsystem.repository.Faculty;

import com.avit.collegemanagementsystem.model.Faculty.Faculty;
import com.avit.collegemanagementsystem.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long > {
    Faculty findByEmail(String email);

    Optional<Faculty> findByUsername(String username);

    boolean existsByUsername(String user1);

    boolean existsByEmail(String email);
}

