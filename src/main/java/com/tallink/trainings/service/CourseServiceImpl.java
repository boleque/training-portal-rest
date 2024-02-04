package com.tallink.trainings.service;

import com.tallink.trainings.exception.EntityNotFoundException;
import com.tallink.trainings.model.Course;
import com.tallink.trainings.model.Participant;
import com.tallink.trainings.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REPORTING')")
    @Transactional(readOnly = true)
    @Override
    public List<Participant> findAllParticipants(long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(courseId))
                .getParticipants();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REPORTING')")
    @Override
    public Course findById(long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(courseId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void save(Course course) {

        courseRepository.save(course);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(long courseId) {
        courseRepository.deleteById(courseId);
    }
}
