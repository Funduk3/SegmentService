package com.fedordemin.segmentservice.Repositories;

import com.fedordemin.segmentservice.Entities.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SegmentRepo extends JpaRepository<Segment, Long> {
    Optional<Segment> findByCode(String code);
}
