package com.avit.collegemanagementsystem.controller.Faculty;

import com.avit.collegemanagementsystem.service.Faculty.interfaces.StudentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api/faculty/")
public class StudentDetailsController {

   @Autowired
   private StudentDetails studentDetails;

    @PostMapping("/all-students")
    public ResponseEntity<?> addStudent() {
        return new ResponseEntity<>(studentDetails.getAllStudents() , HttpStatus.OK);
    }
}
