package com.fedordemin.segmentservice.entitiesTest;

import com.fedordemin.segmentservice.Entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testUserCreation() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUserSegments());
    }

    @Test
    void testUserEquality() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test1@example.com");

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("test2@example.com");

        User user3 = new User();
        user3.setId(2L);
        user3.setEmail("test1@example.com");

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }
}
