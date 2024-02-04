package com.tallink.trainings.service;

import com.tallink.trainings.exception.CourseException;
import com.tallink.trainings.model.Course;
import com.tallink.trainings.model.Participant;

import java.util.List;


public interface ParticipantService {

    List<Participant> findAll();

    List<Course> findAllCourses(long participantId);

    Participant findById(long participantId);

    void save(Participant participant);

    void deleteById(long participantId);

    void enrollToCourse(long participantId, long courseId) throws CourseException;

    void unrollFromCourse(long participantId, long courseId) throws CourseException;
}
