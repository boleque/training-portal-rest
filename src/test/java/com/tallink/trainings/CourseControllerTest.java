package com.tallink.trainings;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallink.trainings.model.Course;
import com.tallink.trainings.model.Participant;
import com.tallink.trainings.rest.CourseController;
import com.tallink.trainings.dto.CourseDto;
import com.tallink.trainings.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldReturnAllCourses() throws Exception {
        final LocalDateTime dateTime = LocalDateTime.of(2024, 2, 2, 14, 33);
        final List<Course> courses = List.of(
                new Course(1L, "Java for Dummies", dateTime, dateTime, "Become a Java programmer in 1 week", 1, 2, null)
        );
        final String expectedResult = "[{\"id\":1,\"title\":\"Java for Dummies\",\"startTime\":\"2024-02-02T14:33:00\",\"endTime\":\"2024-02-02T14:33:00\",\"description\":\"Become a Java programmer in 1 week\",\"currentNumberOfParticipants\":1,\"maxNumberOfParticipants\":2}]";

        given(courseService.findAll()).willReturn(courses);
        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    @Test
    public void shouldReturnParticipantsList() throws Exception {
        final long courseId = 1L;
        final LocalDateTime dateTime = LocalDateTime.of(2024, 2, 2, 14, 33);
        final List<Participant> participants = List.of(new Participant(1L, "Jane Austen", null), new Participant(2L, "George Orwell", null));
        final List<Course> courses = List.of(
                new Course(1L, "Java for Dummies", dateTime, dateTime, "Become a Java programmer in 1 week", 1, 2, participants)
        );
        final String expectedResult = "[{\"id\":1,\"name\":\"Jane Austen\"},{\"id\":2,\"name\":\"George Orwell\"}]";

        given(courseService.findAllParticipants(courseId)).willReturn(participants);
        mockMvc.perform(get(String.format("/course/%s/participants", courseId)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    @Test
    public void shouldReturnCourseByIdInPath() throws Exception {
        final long courseId = 1L;
        final LocalDateTime dateTime = LocalDateTime.of(2024, 2, 2, 14, 33);
        final Course course = new Course(1L, "Java for Dummies", dateTime, dateTime, "Become a Java programmer in 1 week", 1, 2, null);
        final String expectedResult = "{\"id\":1,\"title\":\"Java for Dummies\",\"startTime\":\"2024-02-02T14:33:00\",\"endTime\":\"2024-02-02T14:33:00\",\"description\":\"Become a Java programmer in 1 week\",\"currentNumberOfParticipants\":1,\"maxNumberOfParticipants\":2}";

        given(courseService.findById(courseId)).willReturn(course);
        mockMvc.perform(get(String.format("/course/%s", courseId)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    @Test
    public void shouldCreateCourseByRequestBody() throws Exception {
        final LocalDateTime dateTime = LocalDateTime.of(2024, 2, 2, 14, 33);
        final CourseDto courseDto = new CourseDto(1L, "Java for Dummies", dateTime, dateTime, "Become a Java programmer in 1 week", 1, 2);
        final String request = mapper.writeValueAsString(courseDto);

        mockMvc.perform(post("/course")
                        .content(request)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateCourseByRequestBody() throws Exception {
        final LocalDateTime dateTime = LocalDateTime.of(2024, 2, 2, 14, 33);
        final CourseDto courseDto = new CourseDto(1L, "Java for Dummies", dateTime, dateTime, "Become a Java programmer in 1 week", 1, 2);
        final String request = mapper.writeValueAsString(courseDto);

        mockMvc.perform(put("/course")
                        .content(request)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteCourseById() throws Exception {
        mockMvc.perform(delete("/course/1").with(csrf()))
                .andExpect(status().isOk());
    }
}
