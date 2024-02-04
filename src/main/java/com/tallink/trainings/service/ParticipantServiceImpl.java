package com.tallink.trainings.service;

import com.tallink.trainings.exception.CourseException;
import com.tallink.trainings.exception.EntityNotFoundException;
import com.tallink.trainings.model.Course;
import com.tallink.trainings.model.Participant;
import com.tallink.trainings.repository.CourseRepository;
import com.tallink.trainings.repository.ParticipantRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    private final CourseRepository courseRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'REPORTING')")
    @Override
    public List<Participant> findAll() {
        return participantRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REPORTING')")
    @Transactional(readOnly = true)
    @Override
    public List<Course> findAllCourses(long participantId) {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException(participantId))
                .getCourses();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REPORTING')")
    @Override
    public Participant findById(long participantId) {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException(participantId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void save(Participant participant) {
        participantRepository.save(participant);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(long participantId) {
        courseRepository.deleteById(participantId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void enrollToCourse(long participantId, long courseId) throws CourseException {
        Course course = courseRepository.getReferenceById(courseId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = course.getEndTime();
        if (now.isEqual(endTime) || now.isAfter(endTime)) {
            throw new CourseException("The course %s is expired".formatted(courseId));
        }
        int currentNumberOfParticipants = course.getCurrentNumberOfParticipants();
        int maxNumberOfParticipants = course.getMaxNumberOfParticipants();
        if (currentNumberOfParticipants >= maxNumberOfParticipants) {
            throw new CourseException("The course %s is fully booked".formatted(courseId));
        }
        Participant participant = participantRepository.getReferenceById(participantId);
        List<Course> participantCourses = new ArrayList<>(participant.getCourses());
        boolean isEnrolled = participantCourses.stream().anyMatch(c -> c.getId() == courseId);
        if (isEnrolled) {
            throw new CourseException("Already enrolled to the course %s".formatted(courseId));
        }
        participantCourses.add(course);
        participant.setCourses(participantCourses);
        participantRepository.save(participant);
        course.setCurrentNumberOfParticipants(++currentNumberOfParticipants);
        courseRepository.save(course);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void unrollFromCourse(long participantId, long courseId) throws CourseException {
        Participant participant = participantRepository.getReferenceById(participantId);
        List<Course> participantCourses = new ArrayList<>(participant.getCourses());
        boolean isEnrolled = participantCourses.stream().anyMatch(c -> c.getId() == courseId);
        if (!isEnrolled) {
            throw new CourseException("Participant is not enrolled to course %s".formatted(courseId));
        }
        Course course = courseRepository.getReferenceById(courseId);
        participantCourses.remove(course);
        participant.setCourses(participantCourses);
        participantRepository.save(participant);

        int currentNumberOfParticipants = course.getCurrentNumberOfParticipants();
        course.setCurrentNumberOfParticipants(--currentNumberOfParticipants);
        courseRepository.save(course);
    }
}
