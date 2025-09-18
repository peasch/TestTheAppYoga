package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;

    private Session session;
    private User user;
    @BeforeEach
    public void setup() {

        session = new Session();
        session.setId(1L);
        user = new User();
        user.setId(1L);
        session.setUsers(Arrays.asList());
    }
    @Test
    void service_findAll_Test() {
        //GIVEN
        List<Session> sessionsTocheck = new ArrayList<>();
        when(sessionRepository.findAll()).thenReturn(sessionsTocheck);

        //WHEN
        List<Session> sessions = sessionService.findAll();
        assertThat(sessions).isEqualTo(sessionsTocheck);
    }

    @Test
    void service_getById_Test() {
        //GIVEN

        //WHEN
        Session session = sessionService.getById(1L);
        assertThat(session).isNull();
    }

    @Test
    void participate_NoSession_Test() {
        //GIVEN
        User user = new User();
        user.setId(1L);
        when(this.sessionRepository.findById(any())).thenReturn(null);


        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Vérification de l'exception NotFoundException
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(1L, 2L);
        });
        Session session = new Session();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(1L, 2L);
        });

    }

    @Test
    void participate_already_Test() {
        //GIVEN
        User existingUser = new User();
        existingUser.setId(1L);
        session.setUsers(Arrays.asList(existingUser));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(1L, 1L);
        });
    }
    @Test
    void participate_ok_Test() {
        //GIVEN
        User noExistingUser = new User();
        noExistingUser.setId(2L);
        List<User> users = new ArrayList<>();  // Liste modifiable
        session.setUsers(users);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));


        sessionService.participate(1L, 2L);

    }

    @Test
    void session_update_test(){
        session.setName("newSession name");

        sessionService.update(1L,session);

    }
    @Test
    void no_longer_participate_Test() {
        //GIVEN
        User existingUser = new User();
        existingUser.setId(2L);
        List<User> users = new ArrayList<>();
        users.add(existingUser);
        session.setUsers(users);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));



        sessionService.noLongerParticipate(1L, 2L);

    }

    @Test
    void no_longer_participate_already_Test() {
        //GIVEN
        User existingUser = new User();
        existingUser.setId(1L);
        session.setUsers(Arrays.asList(existingUser));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));


        assertThrows(BadRequestException.class, () -> {
            sessionService.noLongerParticipate(1L, 2L);
        });
    }




}
