package com.openclassrooms.starterjwt.controller.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(SpringExtension.class)
class AuthControllerSITest {
    @Autowired
    private MockMvc mockMvc;

    @Test
     void loginTest() throws Exception {
        // crée un utilisateur admin
        String email = "yoga@studio.com";
        String password = "test!1234";
        String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }


    @Test
     void loginFailureTest() throws Exception {
        // crée un utilisateur admin
        String email = "yoga@studio.com";
        String password = "test!123";
        String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
     void registerTest() throws Exception {
        // crée un utilisateur admin
        String email = "john@john.fr";
        String password = "test!1234";
        String firstName ="John";
        String lastName = "Doe";
        String requestBody = "{" +
                "\"email\": \"" + email + "\"," +
                "\"password\": \"" + password + "\"," +
                "\"firstName\": \"" + firstName + "\"," +
                "\"lastName\": \"" + lastName + "\"" +
                "}";


        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }
    @Test
    void loginToDeleteTest() throws Exception {
        String email = "john@john.fr";
        String password = "test!1234";
        String requestBody = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)).andReturn();

        String content = result.getResponse().getContentAsString();
        String token = JsonPath.read(content, "$.token");
        int id = JsonPath.read(content, "$.id");

        mockMvc.perform(delete("/api/user/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }



}
