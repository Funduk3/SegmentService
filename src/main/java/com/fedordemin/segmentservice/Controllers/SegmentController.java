package com.fedordemin.segmentservice.Controllers;

import com.fedordemin.segmentservice.Entities.Segment;
import com.fedordemin.segmentservice.Services.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fedordemin.segmentservice.DTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/segments")
public class SegmentController {
    private final SegmentService service;

    @Autowired
    public SegmentController(SegmentService service) {
        this.service = service;
    }

    @Operation(summary = "Создать новый сегмент")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Сегмент успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос",
                    ref = "#/components/responses/BadRequest")
    })
    @PostMapping
    public ResponseEntity<SegmentResponse> create(@RequestBody CreateSegmentRequest r) {
        try {
            Segment s = service.createSegment(r);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(s));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Обновить сегмент")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сегмент успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Сегмент не найден",
                    ref = "#/components/responses/NotFound")
    })
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

    @Operation(summary = "Удалить сегмент")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Сегмент успешно удален"),
            @ApiResponse(responseCode = "404", description = "Сегмент не найден",
                    ref = "#/components/responses/NotFound")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.getSegmentById(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        service.deleteSegment(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Добавить пользователей в сегмент")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи успешно добавлены"),
            @ApiResponse(responseCode = "404", description = "Сегмент не найден",
                    ref = "#/components/responses/NotFound")
    })
    @PostMapping("/{id}/users")
    public ResponseEntity<Void> addUsers(@PathVariable Long id,
                                         @RequestBody UserListRequest r) {
        try {
            service.getSegmentById(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        service.addUsersToSegment(id, r.getUserIds());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить пользователей из сегмента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи успешно удалены"),
            @ApiResponse(responseCode = "404", description = "Сегмент не найден",
                    ref = "#/components/responses/NotFound")
    })
    @DeleteMapping("/{id}/users")
    public ResponseEntity<Void> removeUsers(@PathVariable Long id,
                                            @RequestBody UserListRequest r) {
        try {
            service.getSegmentById(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        service.removeUsersFromSegment(id, r.getUserIds());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Назначить процент для сегмента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Процент успешно назначен"),
            @ApiResponse(responseCode = "404", description = "Сегмент не найден",
                    ref = "#/components/responses/NotFound")
    })
    @PostMapping("/{id}/assign")
    public ResponseEntity<Void> assignPercent(@PathVariable Long id,
                                              @RequestBody AssignPercentRequest r) {
        try {
            service.getSegmentById(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
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
