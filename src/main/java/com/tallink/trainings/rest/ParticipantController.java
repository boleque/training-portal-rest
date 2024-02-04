package com.tallink.trainings.rest;


import com.tallink.trainings.model.Participant;
import com.tallink.trainings.dto.CourseDto;
import com.tallink.trainings.dto.ParticipantDto;
import com.tallink.trainings.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("participant")
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @Operation(summary = "Returns a list of participants")
    @GetMapping(value = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ParticipantDto> findAll() {
        return participantService.findAll().stream()
                .map(ParticipantDto::toDto)
                .toList();
    }

    @Operation(summary = "Returns a list of courses for a specified participant ID")
    @GetMapping(value = "{id}/courses", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CourseDto> findAllCourses(@PathVariable("id") long id) {
        return participantService.findAllCourses(id).stream().map(CourseDto::toDto).toList();
    }

    @Operation(summary = "Returns the participant by the specified ID")
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ParticipantDto findById(@PathVariable("id") long id) {
        Participant participant = participantService.findById(id);
        return ParticipantDto.toDto(participant);
    }

    @Operation(summary = "Creates a new participant")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody ParticipantDto participantDto) {
        participantService.save(participantDto.toModel());
    }

    @Operation(summary = "Updates the participant")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody ParticipantDto participantDto) {
        participantService.save(participantDto.toModel());
    }

    @Operation(summary = "Enrolls the participant to the course specified by ID")
    @PostMapping(value = "{id}/enroll")
    public void enrollToCourse(
            @PathVariable("id") long id,
            @RequestParam(value = "courseId") long courseId) {
            participantService.enrollToCourse(id, courseId);
    }

    @Operation(summary = "Unrolls the participant from the course specified by ID")
    @PostMapping(value = "{id}/unroll")
    public void unrollFromCourse(
            @PathVariable("id") long id,
            @RequestParam(value = "courseId") long courseId) {
            participantService.unrollFromCourse(id, courseId);
    }

    @Operation(summary = "Deletes the course by the specified ID")
    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable("id") long id) {
        participantService.deleteById(id);
    }
}
