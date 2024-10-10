package com.avit.collegemanagementsystem.repository.User;

import com.avit.collegemanagementsystem.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long > {
    User findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String user1);

    boolean existsByEmail(String email);
}

