package com.tallink.trainings;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallink.trainings.model.Course;
import com.tallink.trainings.model.Participant;
import com.tallink.trainings.rest.ParticipantController;
import com.tallink.trainings.dto.ParticipantDto;
import com.tallink.trainings.service.ParticipantService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
@WebMvcTest(ParticipantController.class)
public class ParticipantsControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParticipantService participantService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldReturnParticipantsList() throws Exception {
        final List<Participant> participants = List.of(
                new Participant(1L, "Jane Austen", null)
        );
        final String expectedResult = "[{\"id\":1,\"name\":\"Jane Austen\"}]";

        given(participantService.findAll()).willReturn(participants);
        mockMvc.perform(get("/participant/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    @Test
    public void shouldReturnCoursesList() throws Exception {
        final long participantId = 1L;
        final LocalDateTime dateTime = LocalDateTime.of(2024, 2, 2, 14, 33);
        final List<Course> courses = List.of(
                new Course(1L, "Java for Dummies", dateTime, dateTime, "Become a Java programmer in 1 week", 1, 2, null)
        );
        final String expectedResult = "[{\"id\":1,\"title\":\"Java for Dummies\",\"startTime\":\"2024-02-02T14:33:00\",\"endTime\":\"2024-02-02T14:33:00\",\"description\":\"Become a Java programmer in 1 week\",\"currentNumberOfParticipants\":1,\"maxNumberOfParticipants\":2}]";

        given(participantService.findAllCourses(participantId)).willReturn(courses);
        mockMvc.perform(get(String.format("/participant/%s/courses", participantId)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    @Test
    public void shouldReturnParticipantByIdInPath() throws Exception {
        final long participantId = 1L;
        final Participant participant = new Participant(1L, "Jane Austen", null);
        final String expectedResult = "{\"id\":1,\"name\":\"Jane Austen\"}";

        given(participantService.findById(participantId)).willReturn(participant);
        mockMvc.perform(get(String.format("/participant/%s", participantId)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    @Test
    public void shouldCreateParticipantByRequestBody() throws Exception {
        final ParticipantDto participantDto = new ParticipantDto(1L, "Jane Austen");
        final String request = mapper.writeValueAsString(participantDto);

        mockMvc.perform(post("/participant")
                        .content(request)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateParticipantByRequestBody() throws Exception {
        final ParticipantDto participantDto = new ParticipantDto(1L, "Jane Austen");
        final String request = mapper.writeValueAsString(participantDto);

        mockMvc.perform(put("/participant")
                        .content(request)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteParticipantById() throws Exception {
        mockMvc.perform(delete("/participant/1").with(csrf()))
                .andExpect(status().isOk());
    }
}
