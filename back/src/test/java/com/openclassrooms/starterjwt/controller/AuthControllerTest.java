package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;
    @Mock
    private SecurityContext securityContext;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setup() {

        SecurityContextHolder.setContext(securityContext);


    }

    @Test
    void register_User_Test() {
        //GIVEN
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("user@example.com");
        signUpRequest.setFirstName("User");
        signUpRequest.setLastName("USER");
        signUpRequest.setPassword("password");
        when(userRepository.existsByEmail(any())).thenReturn(Boolean.FALSE);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        //WHEN
        ResponseEntity<?> response = authController.registerUser(signUpRequest);


        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void register_User_Exist_Test() {
        //GIVEN
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("user@example.com");
        signUpRequest.setFirstName("User");
        signUpRequest.setLastName("USER");
        signUpRequest.setPassword("password");
        when(userRepository.existsByEmail(any())).thenReturn(Boolean.TRUE);


        //WHEN
        ResponseEntity<?> response = authController.registerUser(signUpRequest);


        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(MessageResponse.class);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertThat(messageResponse.getMessage()).isEqualTo("Error: Email is already taken!");
    }


        @Test
        void authenticate_User_Test() {
            //GIVEN
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("user@example.com");
            loginRequest.setPassword("password");

            UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
            when(userDetails.getUsername()).thenReturn("user@example.com");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
            when(jwtUtils.generateJwtToken(any())).thenReturn("mockToken");
            when(authentication.getPrincipal()).thenReturn(userDetails);

            User mockUser = new User(1L, "user1@mail.com",
                    "User", "USER", "password",
                    false, LocalDateTime.now(), LocalDateTime.now());

            when(userRepository.findByEmail(any())).thenReturn(Optional.of(mockUser));

            //WHEN

            ResponseEntity<?> response = authController.authenticateUser(loginRequest);

            //ThEN
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isInstanceOf(JwtResponse.class);
            JwtResponse jwtResponse = (JwtResponse) response.getBody();
            assertThat(jwtResponse.getToken()).isEqualTo("mockToken");
            assertThat(jwtResponse.getUsername()).isEqualTo("user@example.com");
            assertThat(jwtResponse.getFirstName()).isEqualTo(null);
            assertThat(jwtResponse.getLastName()).isEqualTo(null);


        }


    }
