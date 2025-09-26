package com.openclassrooms.starterjwt.controller.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TeacherControllerSITest {

    @Autowired
    private MockMvc mockMvc;

    private String loggedInToken;
    private int userLoggedInId;
    @BeforeEach
    void setup() throws Exception {
        String email = "yoga@studio.com";
        String password = "test!1234";
        String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andReturn();

        String content = result.getResponse().getContentAsString();
        loggedInToken = JsonPath.read(content, "$.token");
        userLoggedInId = JsonPath.read(content, "$.id");
    }

    @Test
    void find_all_teacher_Test() throws Exception {

        mockMvc.perform(get("/api/teacher")
                        .header("Authorization", "Bearer " + loggedInToken))
                .andExpect(status().isOk());
    }

    @Test
    void find_a_teacher_Test() throws Exception {

        mockMvc.perform(get("/api/teacher/1")
                        .header("Authorization", "Bearer " + loggedInToken))
                .andExpect(status().isOk());
    }

    @Test
    void findUserFailure() throws Exception {


        mockMvc.perform(get("/api/teacher/" + 99)
                        .header("Authorization", "Bearer " + loggedInToken))
                .andExpect(status().isNotFound());

    }
}
