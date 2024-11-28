package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;
    @InjectMocks
    private TeacherService teacherService;

    @Test
    void findById_test(){
        Teacher teacher = new Teacher();
        teacher.setId(3L);

        when(teacherRepository.findById(3L)).thenReturn(Optional.of(teacher));

        teacherService.findById(3L);
    }
    @Test
    void delete_test(){


        teacherService.findAll();
    }
}
