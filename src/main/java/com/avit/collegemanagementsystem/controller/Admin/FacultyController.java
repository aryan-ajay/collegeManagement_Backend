package com.avit.collegemanagementsystem.controller.Admin;

import com.avit.collegemanagementsystem.model.User.User;
import com.avit.collegemanagementsystem.security.UserServiceImpl;
import com.avit.collegemanagementsystem.service.User.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/")
public class FacultyController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createFaculty(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getFacultyById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllFaculty() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

