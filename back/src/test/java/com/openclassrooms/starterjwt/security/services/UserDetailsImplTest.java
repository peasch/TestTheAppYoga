package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;


    @Test
    void loadUserByUsername_Test() {
        //GIVEN
        User mockUser = new User(1L, "user1@mail.com", "User", "USER", "password", false, LocalDateTime.now(), LocalDateTime.now());

        when(userRepository.findByEmail("user1@mail.com")).thenReturn(Optional.of(mockUser));

        //WHEN
        UserDetailsImpl userDetails= (UserDetailsImpl) userDetailsService.loadUserByUsername("user1@mail.com");

        // THEN
        assertThat(userDetails.getUsername()).isEqualTo(mockUser.getEmail());
        assertThat(userDetails.getFirstName()).isEqualTo(mockUser.getFirstName());
        assertThat(userDetails.getLastName()).isEqualTo(mockUser.getLastName());
        assertThat(userDetails.getPassword()).isEqualTo(mockUser.getPassword());

    }


}
