package com.fedordemin.segmentservice.Services;

import com.fedordemin.segmentservice.DTO.*;
import com.fedordemin.segmentservice.Entities.*;
import com.fedordemin.segmentservice.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SegmentService {

    private final SegmentRepo segmentRepo;
    private final UserRepo userRepo;
    private final UserSegmentRepo usRepo;

    @Autowired
    public SegmentService(SegmentRepo segmentRepo,
                          UserRepo userRepo,
                          UserSegmentRepo usRepo) {
        this.segmentRepo = segmentRepo;
        this.userRepo = userRepo;
        this.usRepo = usRepo;
    }

    public void getSegmentById(Long id) {
        segmentRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Segment not found"));
    }

    public Segment createSegment(CreateSegmentRequest r) {
        Segment s = new Segment();
        s.setCode(r.getCode());
        s.setName(r.getName());
        s.setDescription(r.getDescription());
        return segmentRepo.save(s);
    }

    public Segment updateSegment(Long id, CreateSegmentRequest r) {
        Segment s = segmentRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Segment not found"));
        s.setCode(r.getCode());
        s.setName(r.getName());
        s.setDescription(r.getDescription());
        return segmentRepo.save(s);
    }

    public void deleteSegment(Long id) {
        segmentRepo.deleteById(id);
        removeAllUsersFromSegment(id);
    }

    public List<Segment> getUserSegments(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return usRepo.findByUser(user).stream()
                .map(UserSegment::getSegment)
                .collect(Collectors.toList());
    }

    public void addUsersToSegment(Long segmentId, List<Long> userIds) {
        Segment seg = segmentRepo.findById(segmentId)
                .orElseThrow(() -> new NoSuchElementException("Segment not found"));
        for (Long id : userIds) {
            User u = userRepo.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
            UserSegment us = new UserSegment();
            us.setUser(u);
            us.setSegment(seg);
            usRepo.save(us);
        }
    }

    public void removeUsersFromSegment(Long segmentId, List<Long> userIds) {
        for (Long id : userIds) {
            usRepo.deleteById(new UserSegmentId(id, segmentId));
        }
    }

    public void removeAllUsersFromSegment(Long segmentId) {
        Segment segment = segmentRepo.findById(segmentId)
                .orElseThrow(() -> new NoSuchElementException("Segment not found"));

        List<UserSegment> userSegments = usRepo.findBySegment(segment);
        usRepo.deleteAll(userSegments);
    }

    public void assignSegmentPercent(Long segmentId, int percent) {
        Segment segment = segmentRepo.findById(segmentId)
                .orElseThrow(() -> new NoSuchElementException("Segment not found"));

        long total = userRepo.count();
        long toAssign = Math.round(total * percent / 100.0);

        List<User> users = userRepo.findAll();
        Collections.shuffle(users);

        users.stream()
                .limit(toAssign)
                .forEach(user -> {
                    UserSegment userSegment = new UserSegment();
                    userSegment.setUser(user);
                    userSegment.setSegment(segment);
                    usRepo.save(userSegment);
                });
    }
}
