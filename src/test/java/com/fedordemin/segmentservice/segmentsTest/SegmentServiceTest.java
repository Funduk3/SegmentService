package com.fedordemin.segmentservice.segmentsTest;

import com.fedordemin.segmentservice.DTO.CreateSegmentRequest;
import com.fedordemin.segmentservice.Entities.Segment;
import com.fedordemin.segmentservice.Entities.User;
import com.fedordemin.segmentservice.Entities.UserSegment;
import com.fedordemin.segmentservice.Entities.UserSegmentId;
import com.fedordemin.segmentservice.Repositories.SegmentRepo;
import com.fedordemin.segmentservice.Repositories.UserRepo;
import com.fedordemin.segmentservice.Repositories.UserSegmentRepo;
import com.fedordemin.segmentservice.Services.SegmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SegmentServiceTest {

    @Mock
    private SegmentRepo segmentRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserSegmentRepo userSegmentRepo;

    @InjectMocks
    private SegmentService segmentService;

    private Segment testSegment;
    private User testUser;
    private CreateSegmentRequest createRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testSegment = new Segment();
        testSegment.setId(1L);
        testSegment.setCode("TEST");
        testSegment.setName("Test Segment");

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        createRequest = new CreateSegmentRequest();
        createRequest.setCode("TEST");
        createRequest.setName("Test Segment");
    }

    @Test
    void createSegment_Success() {
        when(segmentRepo.save(any(Segment.class))).thenReturn(testSegment);

        Segment result = segmentService.createSegment(createRequest);

        assertNotNull(result);
        assertEquals("TEST", result.getCode());
        assertEquals("Test Segment", result.getName());
        verify(segmentRepo).save(any(Segment.class));
    }

    @Test
    void updateSegment_Success() {
        when(segmentRepo.findById(1L)).thenReturn(Optional.of(testSegment));
        when(segmentRepo.save(any(Segment.class))).thenReturn(testSegment);

        createRequest.setCode("UPDATED");
        Segment result = segmentService.updateSegment(1L, createRequest);

        assertEquals("UPDATED", result.getCode());
        verify(segmentRepo).save(any(Segment.class));
    }

    @Test
    void updateSegment_NotFound() {
        when(segmentRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                segmentService.updateSegment(999L, createRequest));
    }

    @Test
    void getUserSegments_Success() {
        UserSegment userSegment = new UserSegment();
        userSegment.setUser(testUser);
        userSegment.setSegment(testSegment);

        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        when(userSegmentRepo.findByUser(testUser)).thenReturn(Arrays.asList(userSegment));

        List<Segment> results = segmentService.getUserSegments(1L);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("TEST", results.get(0).getCode());
    }

    @Test
    void addUsersToSegment_Success() {
        when(segmentRepo.findById(1L)).thenReturn(Optional.of(testSegment));
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));

        segmentService.addUsersToSegment(1L, Arrays.asList(1L));

        verify(userSegmentRepo).save(any(UserSegment.class));
    }

    @Test
    void removeUsersFromSegment_Success() {
        UserSegmentId id = new UserSegmentId(1L, 1L);
        segmentService.removeUsersFromSegment(1L, Arrays.asList(1L));

        verify(userSegmentRepo).deleteById(id);
    }

    @Test
    void removeAllUsersFromSegment_Success() {
        UserSegment userSegment = new UserSegment();
        userSegment.setUser(testUser);
        userSegment.setSegment(testSegment);
        List<UserSegment> userSegments = Arrays.asList(userSegment);

        when(segmentRepo.findById(1L)).thenReturn(Optional.of(testSegment));
        when(userSegmentRepo.findBySegment(testSegment)).thenReturn(userSegments);

        segmentService.removeAllUsersFromSegment(1L);
        verify(userSegmentRepo).deleteAll(userSegments);
    }

    @Test
    void assignSegmentPercent_Success() {
        when(userRepo.count()).thenReturn(100L);
        when(userRepo.findAll()).thenReturn(Arrays.asList(testUser));
        when(segmentRepo.findById(1L)).thenReturn(Optional.of(testSegment));

        segmentService.assignSegmentPercent(1L, 50);

        verify(userSegmentRepo, atLeastOnce()).save(any(UserSegment.class));
    }

    @Test
    void deleteSegment_Success() {
        UserSegment userSegment = new UserSegment();
        userSegment.setUser(testUser);
        userSegment.setSegment(testSegment);
        List<UserSegment> userSegments = Arrays.asList(userSegment);

        when(segmentRepo.findById(1L)).thenReturn(Optional.of(testSegment));
        when(userSegmentRepo.findBySegment(testSegment)).thenReturn(userSegments);

        segmentService.deleteSegment(1L);

        verify(segmentRepo).deleteById(1L);
        verify(userSegmentRepo).deleteAll(userSegments);

        InOrder inOrder = inOrder(segmentRepo, userSegmentRepo);
        inOrder.verify(segmentRepo).deleteById(1L);
        inOrder.verify(userSegmentRepo).deleteAll(userSegments);
    }
}