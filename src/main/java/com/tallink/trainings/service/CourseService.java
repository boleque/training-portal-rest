package com.tallink.trainings.service;

import com.tallink.trainings.model.Course;
import com.tallink.trainings.model.Participant;

import java.util.List;


public interface CourseService {
    List<Course> findAll();

    List<Participant> findAllParticipants(long courseId);

    Course findById(long courseId);

    void save(Course course);

    void deleteById(long courseId);
}
