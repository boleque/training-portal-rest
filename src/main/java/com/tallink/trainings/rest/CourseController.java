package com.tallink.trainings.rest;

import com.tallink.trainings.model.Course;
import com.tallink.trainings.dto.CourseDto;
import com.tallink.trainings.dto.ParticipantDto;
import com.tallink.trainings.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;


@RestController
@RequestMapping("course")
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Returns a list of courses")
    @GetMapping(value = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CourseDto> findAll() {
        return courseService.findAll().stream()
                .map(CourseDto::toDto)
                .toList();
    }

    @Operation(summary = "Returns a list of participants for a specified course ID")
    @GetMapping(value = "{id}/participants", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ParticipantDto> findAllParticipants(@PathVariable("id") long id) {
        return courseService.findAllParticipants(id).stream()
                .map(ParticipantDto::toDto)
                .toList();
    }

    @Operation(summary = "Returns the course by the specified ID")
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CourseDto findById(@PathVariable("id") long id) {
        Course course = courseService.findById(id);
        return CourseDto.toDto(course);
    }

    @Operation(summary = "Creates a new course")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody CourseDto courseDto) {
        courseService.save(courseDto.toModel());
    }

    @Operation(summary = "Updates the course")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody CourseDto courseDto) {
        courseService.save(courseDto.toModel());
    }

    @Operation(summary = "Deletes the course by the specified ID")
    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable("id") long id) {
        courseService.deleteById(id);
    }
}
