package com.avit.collegemanagementsystem.service.Faculty;

import com.avit.collegemanagementsystem.model.User.User;
import com.avit.collegemanagementsystem.service.Faculty.interfaces.StudentDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentDetailsImpl implements StudentDetails {

    @Override
    public User addStudent(User user) {
        return null;
    }

    @Override
    public List<User> getAllStudents() {
        return List.of();
    }
}
