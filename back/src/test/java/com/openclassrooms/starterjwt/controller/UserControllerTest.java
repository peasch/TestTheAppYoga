package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    private User mockUser;

    @BeforeEach
    public void setup() {

        SecurityContextHolder.setContext(securityContext);

        this.mockUser = new User(1L, "user1@mail.com", "User", "USER", "password", false, LocalDateTime.now(), LocalDateTime.now());

    }

    @Test
    void findByIdSuccessTest() {

        // GIVEN
        String userId = "1";
        User user = this.mockUser;
        UserDto userDto = new UserDto();
        when(userService.findById(anyLong())).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);


        // WHEN
        ResponseEntity<?> response = userController.findById(userId);


        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(this.userMapper.toDto(user));
    }

    @Test
    void findByIdErrorTest() {

        // GIVEN
        String userId = "100";

        when(userService.findById(anyLong())).thenReturn(null);



        // WHEN
        ResponseEntity<?> response = userController.findById(userId);


        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void deleteUser_BadRequest_Test() {

        // GIVEN
        String userId = "100";

        when(userService.findById(anyLong())).thenReturn(null);



        // WHEN
        ResponseEntity<?> response = userController.findById(userId);


        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

}
