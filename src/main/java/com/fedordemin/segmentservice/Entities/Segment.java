package com.fedordemin.segmentservice.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "segments")
@Data
public class Segment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String name;
    private String description;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @OneToMany(
            mappedBy = "segment",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private Set<UserSegment> userSegments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Segment s)) return false;
        return Objects.equals(id, s.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
