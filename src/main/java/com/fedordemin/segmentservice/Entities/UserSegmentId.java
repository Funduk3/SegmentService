package com.fedordemin.segmentservice.Entities;

import java.io.Serializable;
import java.util.Objects;

public class UserSegmentId implements Serializable {
    private Long user;
    private Long segment;

    public UserSegmentId() {}

    public UserSegmentId(Long user, Long segment) {
        this.user = user;
        this.segment = segment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSegmentId that)) return false;
        return Objects.equals(user, that.user)
                && Objects.equals(segment, that.segment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, segment);
    }
}


