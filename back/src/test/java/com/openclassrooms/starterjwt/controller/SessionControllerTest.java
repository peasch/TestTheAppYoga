package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import com.sun.jdi.LongValue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

    @Mock
    private SessionService sessionService;
    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private Session mockSession;
    List<User> mockUsers = new ArrayList<>();

    @BeforeEach
    public void setup() {

        mockUsers.add(new User(1L, "user1@mail.com", "User", "USER", "password", false, LocalDateTime.now(), LocalDateTime.now()));
        mockUsers.add(new User(2L, "user2@mail.com", "Test", "TEST", "password", true, LocalDateTime.now(), LocalDateTime.now()));

        Teacher mockTeacher = new Teacher(1L, "Toto", "TOTO", LocalDateTime.now(), LocalDateTime.now());

        this.mockSession = new Session(1L, "Session 1", new Date(), "description 1", mockTeacher, mockUsers, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void not_find_session_Test() {
        //GIVEN

        when(sessionService.getById(10L)).thenReturn(null);

        //WHEN
        ResponseEntity<?> response = sessionController.findById(String.valueOf(10L));

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void find_byId_session_Test() {
        //GIVEN

        when(sessionService.getById(10L)).thenReturn(mockSession);

        //WHEN
        ResponseEntity<?> response = sessionController.findById(String.valueOf(10L));

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void find_all_sessions_Test() {
        //GIVEN

        List<Session> sessions = List.of(this.mockSession);
        when(sessionService.findAll()).thenReturn(sessions);


        //WHEN
        ResponseEntity<?> response = sessionController.findAll();

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void create_session_Test() {
        //GIVEN
        SessionDto sessionDto = mock(SessionDto.class);
        Session session = new Session();
        Session mockSession = mock(Session.class);
        when(sessionMapper.toEntity((SessionDto) any())).thenReturn(mockSession);
        when(sessionService.create(any())).thenReturn(mockSession);
        Session.builder().toString();
        log.debug(session.toString());

        //WHEN
        ResponseEntity<?> response = sessionController.create(sessionDto);

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void update_session_Test() {
        //GIVEN
        String sessionId  ="1";
        SessionDto sessionDto = mock(SessionDto.class);

        when(sessionMapper.toEntity((SessionDto) any())).thenReturn(mockSession);
        when(sessionService.update(Long.valueOf(sessionId), this.mockSession)).thenReturn(mockSession);

        //WHEN
        ResponseEntity<?> response = sessionController.update(sessionId, sessionDto);

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void delete_session_Test() {
        String sessionId  ="1";

        when(sessionService.getById(Long.valueOf(sessionId))).thenReturn(mockSession);

        //WHEN
        ResponseEntity response =sessionController.save(sessionId);
        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void delete_not_found_session_Test() {
        String sessionId  ="1";

        when(sessionService.getById(Long.valueOf(sessionId))).thenReturn(null);

        //WHEN
        ResponseEntity response =sessionController.save(sessionId);
        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void participate_session_Test() {

        User mockUser =mock(User.class);
        mockUser.setId(10L);


        //WHEN
        ResponseEntity response =sessionController.participate(mockSession.getId().toString(),mockUser.getId().toString());

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void no_longer_participate_session_Test() {

        User mockUser =mock(User.class);
        mockUser.setId(10L);


        //WHEN
        ResponseEntity response =sessionController.noLongerParticipate(mockSession.getId().toString(),mockUser.getId().toString());

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
