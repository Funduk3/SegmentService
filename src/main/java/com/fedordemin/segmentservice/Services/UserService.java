package com.fedordemin.segmentservice.Services;

import com.fedordemin.segmentservice.DTO.SegmentResponse;
import com.fedordemin.segmentservice.DTO.UserDto;
import com.fedordemin.segmentservice.Entities.User;
import com.fedordemin.segmentservice.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final SegmentService segService;

    @Autowired
    public UserService(UserRepo userRepo, SegmentService segmentService) {
        this.userRepo = userRepo;
        this.segService = segmentService;
    }

    public void createUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        userRepo.save(user);
    }

    public List<SegmentResponse> getUserSegments(Long id) {
        return segService.getUserSegments(id).stream()
                .map(s -> {
                    SegmentResponse dto = new SegmentResponse();
                    dto.setId(s.getId());
                    dto.setCode(s.getCode());
                    dto.setName(s.getName());
                    dto.setDescription(s.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
