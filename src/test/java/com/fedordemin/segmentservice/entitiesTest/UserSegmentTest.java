package com.fedordemin.segmentservice.entitiesTest;

import com.fedordemin.segmentservice.Entities.Segment;
import com.fedordemin.segmentservice.Entities.User;
import com.fedordemin.segmentservice.Entities.UserSegment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSegmentTest {
    @Test
    void testUserSegmentCreation() {
        User user = new User();
        user.setId(1L);

        Segment segment = new Segment();
        segment.setId(1L);

        UserSegment userSegment = new UserSegment();
        userSegment.setUser(user);
        userSegment.setSegment(segment);

        assertEquals(user, userSegment.getUser());
        assertEquals(segment, userSegment.getSegment());
    }

    @Test
    void testUserSegmentEquality() {
        User user1 = new User();
        user1.setId(1L);
        Segment segment1 = new Segment();
        segment1.setId(1L);

        User user2 = new User();
        user2.setId(2L);
        Segment segment2 = new Segment();
        segment2.setId(2L);

        UserSegment us1 = new UserSegment();
        us1.setUser(user1);
        us1.setSegment(segment1);

        UserSegment us2 = new UserSegment();
        us2.setUser(user1);
        us2.setSegment(segment1);

        UserSegment us3 = new UserSegment();
        us3.setUser(user2);
        us3.setSegment(segment2);

        assertEquals(us1, us2);
        assertNotEquals(us1, us3);
        assertEquals(us1.hashCode(), us2.hashCode());
        assertNotEquals(us1.hashCode(), us3.hashCode());
    }
}