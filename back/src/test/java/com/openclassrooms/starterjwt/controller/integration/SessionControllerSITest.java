package com.openclassrooms.starterjwt.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.dto.SessionDto;
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

import java.time.LocalDateTime;

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

        MvcResult result1 = login();


        String content1 = result1.getResponse().getContentAsString();
        String token1 = JsonPath.read(content1, "$.token");
        int id = 3;


        mockMvc.perform(get("/api/session/"+id)
                        .header("Authorization", "Bearer " + token1))
                .andExpect(status().isOk());
    }
}
