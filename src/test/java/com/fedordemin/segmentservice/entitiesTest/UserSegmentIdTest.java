package com.fedordemin.segmentservice.entitiesTest;


import com.fedordemin.segmentservice.Entities.UserSegmentId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSegmentIdTest {
    @Test
    void testUserSegmentIdCreation() {
        UserSegmentId id = new UserSegmentId(1L, 1L);
        assertNotNull(id);
    }

    @Test
    void testUserSegmentIdEquality() {
        UserSegmentId id1 = new UserSegmentId(1L, 1L);
        UserSegmentId id2 = new UserSegmentId(1L, 1L);
        UserSegmentId id3 = new UserSegmentId(2L, 2L);

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void testDefaultConstructor() {
        UserSegmentId id = new UserSegmentId();
        assertNotNull(id);
    }
}
