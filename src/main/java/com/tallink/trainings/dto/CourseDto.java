package com.tallink.trainings.dto;

import com.tallink.trainings.model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private Long id;

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String description;

    private int currentNumberOfParticipants;

    private int maxNumberOfParticipants;

    public static CourseDto toDto(Course course) {
        return new CourseDto(
                course.getId(),
                course.getTitle(),
                course.getStartTime(),
                course.getEndTime(),
                course.getDescription(),
                course.getCurrentNumberOfParticipants(),
                course.getMaxNumberOfParticipants()
        );
    }

    public Course toModel() {
        return new Course(
                getId(),
                getTitle(),
                getStartTime(),
                getEndTime(),
                getDescription(),
                getCurrentNumberOfParticipants(),
                getMaxNumberOfParticipants(),
                null
        );
    }
}
