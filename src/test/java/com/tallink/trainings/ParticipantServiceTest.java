package com.tallink.trainings;

import com.tallink.trainings.exception.CourseException;
import com.tallink.trainings.model.Course;
import com.tallink.trainings.model.Participant;
import com.tallink.trainings.repository.CourseRepository;
import com.tallink.trainings.repository.ParticipantRepository;
import com.tallink.trainings.service.ParticipantService;
import com.tallink.trainings.service.ParticipantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {ParticipantServiceImpl.class} )
@ExtendWith({SpringExtension.class})
public class ParticipantServiceTest {

    @Autowired
    private ParticipantService participantService;

    @MockBean
    CourseRepository courseRepository;

    @MockBean
    ParticipantRepository participantRepository;

    @Test
    public void enrollToExpiredCourse() {
        final long expiredCourseId = 1L;
        final long participantId = 2L;
        final LocalDateTime startDt = LocalDateTime.of(2022, 2, 2, 14, 33);
        final LocalDateTime endDt = LocalDateTime.of(2023, 2, 2, 14, 33);
        final Course course = new Course(
                1L,
                "Java for Dummies",
                startDt,
                endDt, // specify last already passed 2023
                "Become a Java programmer in 1 week",
                1,
                2,
                null
        );
        given(courseRepository.getReferenceById(expiredCourseId)).willReturn(course);

        assertThrows(CourseException.class, () -> participantService.enrollToCourse(participantId, expiredCourseId));
    }

    @Test
    public void enrollToFullyBookedCourse() {
        final long expiredCourseId = 1L;
        final long participantId = 2L;
        final LocalDateTime startDt = LocalDateTime.of(2022, 2, 2, 14, 33);
        final LocalDateTime endDt = LocalDateTime.of(2030, 2, 2, 14, 33);
        final Course course = new Course(
                1L,
                "Java for Dummies",
                startDt,
                endDt,
                "Become a Java programmer in 1 week",
                2, // same value
                2, // same value
                null
        );
        given(courseRepository.getReferenceById(expiredCourseId)).willReturn(course);
        assertThrows(CourseException.class, () -> participantService.enrollToCourse(participantId, expiredCourseId));
    }

    @Test
    public void enrollToAlreadyParticipatingCourse() {
        final long courseId = 1L;
        final long participantId = 2L;
        final LocalDateTime startDt = LocalDateTime.of(2022, 2, 2, 14, 33);
        final LocalDateTime endDt = LocalDateTime.of(2030, 2, 2, 14, 33);
        final Course course = new Course(
                1L, // the same id as courseId
                "Java for Dummies",
                startDt,
                endDt,
                "Become a Java programmer in 1 week",
                1,
                2,
                null
        );
        final Participant participant = new Participant(1L, "'Charles Dickens", List.of(course));
        given(courseRepository.getReferenceById(courseId)).willReturn(course);
        given(participantRepository.getReferenceById(participantId)).willReturn(participant);

        assertThrows(CourseException.class, () -> participantService.enrollToCourse(participantId, courseId));
    }

    @Test
    public void unrollFromNotParticipatedCourse() {
        final long courseId = 1L;
        final long participantId = 2L;
        final Course course = new Course(
                3L, // different from courseId
                "Java for Dummies",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Become a Java programmer in 1 week",
                1,
                2,
                null
        );
        final Participant participant = new Participant(1L, "'Charles Dickens", List.of(course));
        given(participantRepository.getReferenceById(participantId)).willReturn(participant);

        assertThrows(CourseException.class, () -> participantService.unrollFromCourse(participantId, courseId));
    }
}

