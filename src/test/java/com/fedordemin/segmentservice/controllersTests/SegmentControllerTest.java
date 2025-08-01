package com.fedordemin.segmentservice.controllersTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedordemin.segmentservice.Controllers.SegmentController;
import com.fedordemin.segmentservice.DTO.*;
import com.fedordemin.segmentservice.Entities.Segment;
import com.fedordemin.segmentservice.Services.SegmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SegmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SegmentService segmentService;

    @InjectMocks
    private SegmentController segmentController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(segmentController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createSegment_Success() throws Exception {
        CreateSegmentRequest request = new CreateSegmentRequest();
        request.setCode("TEST");
        request.setName("Test Segment");

        Segment segment = new Segment();
        segment.setId(1L);
        segment.setCode("TEST");
        segment.setName("Test Segment");

        when(segmentService.createSegment(any())).thenReturn(segment);

        mockMvc.perform(post("/api/segments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("TEST"));

        verify(segmentService).createSegment(any());
    }

    @Test
    void updateSegment_Success() throws Exception {
        CreateSegmentRequest request = new CreateSegmentRequest();
        request.setCode("UPDATED");

        Segment segment = new Segment();
        segment.setId(1L);
        segment.setCode("UPDATED");

        when(segmentService.updateSegment(eq(1L), any())).thenReturn(segment);

        mockMvc.perform(patch("/api/segments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("UPDATED"));
    }

    @Test
    void updateSegment_NotFound() throws Exception {
        CreateSegmentRequest request = new CreateSegmentRequest();
        request.setCode("UPDATED");

        when(segmentService.updateSegment(eq(1L), any()))
                .thenThrow(new NoSuchElementException("Segment not found"));

        mockMvc.perform(patch("/api/segments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSegment_Success() throws Exception {
        doNothing().when(segmentService).deleteSegment(1L);

        mockMvc.perform(delete("/api/segments/1"))
                .andExpect(status().isNoContent());

        verify(segmentService).deleteSegment(1L);
    }

    @Test
    void addUsersToSegment_Success() throws Exception {
        UserListRequest request = new UserListRequest();
        request.setUserIds(Arrays.asList(1L, 2L));

        doNothing().when(segmentService).addUsersToSegment(anyLong(), anyList());

        mockMvc.perform(post("/api/segments/1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(segmentService).addUsersToSegment(eq(1L), eq(Arrays.asList(1L, 2L)));
    }

    @Test
    void removeUsersFromSegment_Success() throws Exception {
        UserListRequest request = new UserListRequest();
        request.setUserIds(Arrays.asList(1L, 2L));

        mockMvc.perform(delete("/api/segments/1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(segmentService).removeUsersFromSegment(eq(1L), eq(Arrays.asList(1L, 2L)));
    }

    @Test
    void assignPercent_Success() throws Exception {
        AssignPercentRequest request = new AssignPercentRequest();
        request.setPercent(50);

        mockMvc.perform(post("/api/segments/1/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(segmentService).assignSegmentPercent(eq(1L), eq(50));
    }
}