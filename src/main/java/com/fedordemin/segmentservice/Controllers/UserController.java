package com.fedordemin.segmentservice.Controllers;

import com.fedordemin.segmentservice.DTO.SegmentResponse;
import com.fedordemin.segmentservice.DTO.UserDto;
import com.fedordemin.segmentservice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(summary = "Создать новых пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователи успешно созданы"),
            @ApiResponse(responseCode = "400", description = "Ошибка при создании пользователей",
                    ref = "#/components/responses/BadRequest")
    })
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

    @Operation(summary = "Получить сегменты пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список сегментов пользователя успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    ref = "#/components/responses/NotFound")
    })
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

