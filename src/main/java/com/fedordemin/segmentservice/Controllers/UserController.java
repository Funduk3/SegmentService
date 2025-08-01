package com.fedordemin.segmentservice.Controllers;

import com.fedordemin.segmentservice.DTO.SegmentResponse;
import com.fedordemin.segmentservice.DTO.UserDto;
import com.fedordemin.segmentservice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<List<UserDto>> createUser(@RequestBody List<UserDto> dto) {
        try {
            for (UserDto ud: dto) {
                userService.createUser(ud);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<SegmentResponse>> getUserSegments(@PathVariable Long id) {
        try {
            List<SegmentResponse> segments = userService.getUserSegments(id);
            return ResponseEntity.ok(segments);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

