package com.fedordemin.segmentservice.controllersTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedordemin.segmentservice.Controllers.UserController;
import com.fedordemin.segmentservice.DTO.SegmentResponse;
import com.fedordemin.segmentservice.DTO.UserDto;
import com.fedordemin.segmentservice.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;
    private UserDto testUserDto;
    private List<UserDto> testUsers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setEmail("test@example.com");
        testUsers = Arrays.asList(testUserDto);
    }

    @Test
    void createUser_Success() throws Exception {
        doNothing().when(userService).createUser(any(UserDto.class));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUsers)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(userService).createUser(any(UserDto.class));
    }

    @Test
    void createUser_EmptyList() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.emptyList())))
                .andExpect(status().isCreated());

        verify(userService, never()).createUser(any());
    }

    @Test
    void createUser_Failure_ServiceException() throws Exception {
        doThrow(new RuntimeException("Ошибка создания пользователя"))
                .when(userService).createUser(any());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUsers)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserSegments_Success() throws Exception {
        SegmentResponse segmentResponse = new SegmentResponse();
        segmentResponse.setId(1L);
        segmentResponse.setCode("TEST");
        segmentResponse.setName("Test Segment");
        segmentResponse.setDescription("Test Description");

        when(userService.getUserSegments(1L)).thenReturn(Arrays.asList(segmentResponse));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].code").value("TEST"))
                .andExpect(jsonPath("$[0].name").value("Test Segment"))
                .andExpect(jsonPath("$[0].description").value("Test Description"));
    }

    @Test
    void getUserSegments_UserNotFound() throws Exception {
        when(userService.getUserSegments(999L))
                .thenThrow(new NoSuchElementException("User not found"));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserSegments_EmptyList() throws Exception {
        when(userService.getUserSegments(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}