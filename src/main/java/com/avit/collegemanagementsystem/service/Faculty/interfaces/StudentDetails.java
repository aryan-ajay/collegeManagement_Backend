package com.avit.collegemanagementsystem.service.Faculty.interfaces;

import com.avit.collegemanagementsystem.model.User.User;

import java.util.List;

public interface StudentDetails {
    User addStudent(User user);
    List<User> getAllStudents();
}
