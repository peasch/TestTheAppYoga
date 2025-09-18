package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void findById_test(){
        User user = new User();
        user.setId(3L);

        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        userService.findById(3L);
    }

    @Test
    void delete_test(){
        User user = new User();
        user.setId(3L);


        userService.delete(3L);
    }

}
