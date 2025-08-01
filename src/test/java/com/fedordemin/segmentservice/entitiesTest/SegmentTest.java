package com.fedordemin.segmentservice.entitiesTest;

import com.fedordemin.segmentservice.Entities.Segment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SegmentTest {
    @Test
    void testSegmentCreation() {
        Segment segment = new Segment();
        segment.setId(1L);
        segment.setCode("TEST");
        segment.setName("Test Segment");
        segment.setDescription("Test Description");

        assertEquals(1L, segment.getId());
        assertEquals("TEST", segment.getCode());
        assertEquals("Test Segment", segment.getName());
        assertEquals("Test Description", segment.getDescription());
        assertNotNull(segment.getCreatedAt());
        assertNotNull(segment.getUserSegments());
    }

    @Test
    void testSegmentEquality() {
        Segment segment1 = new Segment();
        segment1.setId(1L);
        segment1.setCode("TEST1");

        Segment segment2 = new Segment();
        segment2.setId(1L);
        segment2.setCode("TEST2");

        Segment segment3 = new Segment();
        segment3.setId(2L);
        segment3.setCode("TEST1");

        assertEquals(segment1, segment2);
        assertNotEquals(segment1, segment3);
        assertEquals(segment1.hashCode(), segment2.hashCode());
        assertNotEquals(segment1.hashCode(), segment3.hashCode());
    }
}
