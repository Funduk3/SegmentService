package com.fedordemin.segmentservice.segmentsTest;

import com.fedordemin.segmentservice.DTO.SegmentResponse;
import com.fedordemin.segmentservice.DTO.UserDto;
import com.fedordemin.segmentservice.Entities.Segment;
import com.fedordemin.segmentservice.Entities.User;
import com.fedordemin.segmentservice.Repositories.UserRepo;
import com.fedordemin.segmentservice.Services.SegmentService;
import com.fedordemin.segmentservice.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private SegmentService segmentService;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;
    private Segment testSegment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setEmail("test@example.com");

        testSegment = new Segment();
        testSegment.setId(1L);
        testSegment.setCode("TEST");
        testSegment.setName("Test Segment");
        testSegment.setDescription("Test Description");
    }

    @Test
    void createUser_Success() {
        when(userRepo.save(any(User.class))).thenReturn(testUser);

        userService.createUser(testUserDto);

        verify(userRepo).save(any(User.class));
    }

    @Test
    void createUser_WithNullEmail() {
        UserDto nullEmailDto = new UserDto();
        nullEmailDto.setId(1L);

        when(userRepo.save(any(User.class))).thenReturn(testUser);

        userService.createUser(nullEmailDto);

        verify(userRepo).save(any(User.class));
    }

    @Test
    void getUserSegments_Success() {
        when(segmentService.getUserSegments(1L))
                .thenReturn(Arrays.asList(testSegment));

        List<SegmentResponse> results = userService.getUserSegments(1L);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());

        SegmentResponse response = results.get(0);
        assertEquals(testSegment.getId(), response.getId());
        assertEquals(testSegment.getCode(), response.getCode());
        assertEquals(testSegment.getName(), response.getName());
        assertEquals(testSegment.getDescription(), response.getDescription());
    }

    @Test
    void getUserSegments_EmptyList() {
        when(segmentService.getUserSegments(1L))
                .thenReturn(Collections.emptyList());

        List<SegmentResponse> results = userService.getUserSegments(1L);

        assertTrue(results.isEmpty());
    }

    @Test
    void getUserSegments_UserNotFound() {
        when(segmentService.getUserSegments(999L))
                .thenThrow(new NoSuchElementException("User not found"));

        assertThrows(NoSuchElementException.class, () ->
                userService.getUserSegments(999L));
    }

    @Test
    void getUserSegments_WithNullFields() {
        Segment nullSegment = new Segment();
        nullSegment.setId(2L);

        when(segmentService.getUserSegments(1L))
                .thenReturn(Arrays.asList(nullSegment));

        List<SegmentResponse> results = userService.getUserSegments(1L);

        assertFalse(results.isEmpty());
        SegmentResponse response = results.get(0);
        assertEquals(2L, response.getId());
        assertNull(response.getCode());
        assertNull(response.getName());
        assertNull(response.getDescription());
    }
}