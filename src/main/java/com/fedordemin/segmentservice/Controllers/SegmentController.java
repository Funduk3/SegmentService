package com.fedordemin.segmentservice.Controllers;

import com.fedordemin.segmentservice.Entities.Segment;
import com.fedordemin.segmentservice.Services.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fedordemin.segmentservice.DTO.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/segments")
public class SegmentController {
    private final SegmentService service;

    @Autowired
    public SegmentController(SegmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SegmentResponse> create(@RequestBody CreateSegmentRequest r) {
        Segment s = service.createSegment(r);
        return ResponseEntity.status(201).body(toDto(s));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SegmentResponse> update(@PathVariable Long id,
                                                  @RequestBody CreateSegmentRequest r) {
        try {
            Segment segment = service.updateSegment(id, r);
            return ResponseEntity.ok(toDto(segment));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // каскадное удаление должно быть всего что связано
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteSegment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/users")
    public ResponseEntity<Void> addUsers(@PathVariable Long id,
                                         @RequestBody UserListRequest r) {
        service.addUsersToSegment(id, r.getUserIds());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/users")
    public ResponseEntity<Void> removeUsers(@PathVariable Long id,
                                            @RequestBody UserListRequest r) {
        service.removeUsersFromSegment(id, r.getUserIds());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Void> assignPercent(@PathVariable Long id,
                                              @RequestBody AssignPercentRequest r) {
        service.assignSegmentPercent(id, r.getPercent());
        return ResponseEntity.ok().build();
    }

    private SegmentResponse toDto(Segment s) {
        SegmentResponse dto = new SegmentResponse();
        dto.setId(s.getId());
        dto.setCode(s.getCode());
        dto.setName(s.getName());
        dto.setDescription(s.getDescription());
        return dto;
    }
}
