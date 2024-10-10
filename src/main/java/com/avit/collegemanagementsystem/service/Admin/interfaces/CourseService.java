package com.avit.collegemanagementsystem.service.Admin.interfaces;

import com.avit.collegemanagementsystem.model.Admin.Course;

import java.util.List;

public interface CourseService {
    public Course createCourse(Course course);
    public Course getCourseById(Long id);
    public List<Course> getAllCourses();
    public void deleteCourse(Long id);
}
