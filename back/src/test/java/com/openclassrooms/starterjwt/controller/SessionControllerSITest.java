package com.openclassrooms.starterjwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class SessionControllerSITest {

    @Autowired
    private MockMvc mockMvc;

     final SessionDto createSessionRequest = SessionDto.builder()
            .name("Test Session create")
            .date(java.sql.Timestamp.valueOf(LocalDateTime.now()))
            .teacher_id(2L)
            .description("Test session creation")
            .build();


    MvcResult login() throws Exception {
        String email = "yoga@studio.com";
        String password = "test!1234";
        String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        return mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andReturn();
    }





    @Test
    void modifyASessionTest() throws Exception {

        MvcResult result = login();
        int id = 4;
        ObjectMapper mapper = new ObjectMapper();

        String content = result.getResponse().getContentAsString();
        String token = JsonPath.read(content, "$.token");

        ResultActions sessionResult = mockMvc.perform(get("/api/session/"+id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        String sessionString = sessionResult.andReturn().getResponse().getContentAsString();
        System.out.println("sessionString: "+sessionString);
        int sessId = JsonPath.read(sessionString, "$.id");
        SessionDto modifiedSession = createSessionRequest;
        modifiedSession.setName("Modified session name");
        mockMvc.perform(put("/api/session/"+sessId)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(mapper.writeValueAsString(modifiedSession)))
                .andExpectAll(status().isOk());


    }

    @Test
    void createAndDeleteASessionTest() throws Exception {

        MvcResult result2 = login();
        String name = "wrong session";
        long teacherID = 1L;
        String description = "xmas description";
        String date = "2025-12-25";

        String body = "{" +
                "\"name\": \"" + name + "\"," +
                "\"teacher_id\":" + teacherID + "," +
                "\"description\":\"" + description + "\"," +
                "\"date\":\"" + date + "\"}";

        String content = result2.getResponse().getContentAsString();
        String token2 = JsonPath.read(content, "$.token");


        ResultActions sessionResult = mockMvc.perform(post("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + token2))
                .andExpect(status().isOk());

        String sessionString = sessionResult.andReturn().getResponse().getContentAsString();
        System.out.println("sessionString: "+sessionString);
        int sessId = JsonPath.read(sessionString, "$.id");

        mockMvc.perform(delete("/api/session/"+sessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + token2))
                .andExpect(status().isOk());

    }
    @Test
    void findASessionWithIdTest() throws Exception {

        String email = "jack@jack.fr";
        String password = "jack!1234";
        String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        MvcResult result1 = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andReturn();


        String content1 = result1.getResponse().getContentAsString();
        String token1 = JsonPath.read(content1, "$.token");
        int id = 3;


        mockMvc.perform(get("/api/session/"+id)
                        .header("Authorization", "Bearer " + token1))
                .andExpect(status().isOk());
    }
}
