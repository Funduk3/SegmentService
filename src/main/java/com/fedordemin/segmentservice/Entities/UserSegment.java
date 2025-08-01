package com.fedordemin.segmentservice.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Table(name = "user_segments")
@IdClass(UserSegmentId.class)
@Data
public class UserSegment {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "segment_id", nullable = false)
    private Segment segment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSegment us)) return false;
        return Objects.equals(user.getId(), us.user.getId())
                && Objects.equals(segment.getId(), us.segment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), segment.getId());
    }
}
