package com.openclassrooms.starterjwt.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(SpringExtension.class)
class AuthControllerSITest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private User adminUser;
    private String adminJwtToken;

    @BeforeEach
    void setUp() {
        Optional<User> adminUserOptional = userRepository.findById(1L);
        adminUser = adminUserOptional.orElseGet(() -> {
            User user = new User();
            user.setId(1L);
            user.setAdmin(true);
            user.setCreatedAt(null);
            user.setEmail("yoga@studio.com");
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setPassword("$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq");
            user.setUpdatedAt(null);
            return userRepository.save(user);
        });

        // Authentification comme adminUser, génération du token JWT et set du token dans le SecurityContext
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminUser.getEmail(), "test!1234"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        adminJwtToken = jwtUtils.generateJwtToken(authentication);
    }
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
    @Test
    void testAuthenticateUser_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(responseBody, JwtResponse.class);

        assertEquals("yoga@studio.com", jwtResponse.getUsername());
        assertEquals("Admin", jwtResponse.getFirstName());
        assertEquals("Admin", jwtResponse.getLastName());
        assertFalse(jwtResponse.getToken().isEmpty());
    }


}
