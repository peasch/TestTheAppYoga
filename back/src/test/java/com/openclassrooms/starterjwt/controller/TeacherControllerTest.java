package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private TeacherMapper teacherMapper;
    @InjectMocks
    private TeacherController teacherController;

    private Teacher mockTeacher;

    @BeforeEach
    public void setup() {

        SecurityContextHolder.setContext(securityContext);

        this.mockTeacher = new Teacher(1L,"firstName","lastName", LocalDateTime.now(),LocalDateTime.now());

    }

    @Test
    void _not_found_teacher_by_id_test(){

        //GIVEN
        String id ="1";
        Teacher teach = new Teacher();
        teach.setId(3L);
        teach.setFirstName("firstName");
        teach.setLastName("lastName");
        teach.setCreatedAt(LocalDateTime.now());
        teach.setUpdatedAt(LocalDateTime.now());
        Teacher.builder().toString();
        log.debug(teach.toString());
        Long longId = Long.valueOf(id);
        when(teacherService.findById(any())).thenReturn(mockTeacher);

        //WHEN
        ResponseEntity response = teacherController.findById(id);

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);


    }

    @Test
    void found_teacher_by_id_test(){

        //GIVEN

        when(teacherService.findById(100L)).thenReturn(null);

        //WHEN
        ResponseEntity response = teacherController.findById(String.valueOf(100L));

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);


    }

    @Test
    void find_all_teachers_Test() {
        //GIVEN

        List<Teacher> teachers = List.of(this.mockTeacher);

        when(teacherService.findAll()).thenReturn(teachers);


        //WHEN
        ResponseEntity<?> response = teacherController.findAll();

        //THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


}
