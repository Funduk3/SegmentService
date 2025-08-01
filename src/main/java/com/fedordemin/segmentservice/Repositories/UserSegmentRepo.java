package com.fedordemin.segmentservice.Repositories;
import com.fedordemin.segmentservice.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSegmentRepo extends JpaRepository<UserSegment, UserSegmentId> {
    List<UserSegment> findByUser(User user);
    List<UserSegment> findBySegment(Segment segment);
}
