package com.schoolerp.controller;

import com.schoolerp.dto.request.TimetableRequest;
import com.schoolerp.dto.response.ApiResponse;
import com.schoolerp.dto.response.TimetableResponse;
import com.schoolerp.service.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/timetable")
@RequiredArgsConstructor
@Tag(name = "Timetable", description = "Timetable management endpoints")
public class TimetableController {

    private final TimetableService timetableService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a timetable entry")
    public ResponseEntity<ApiResponse<TimetableResponse>> createEntry(@Valid @RequestBody TimetableRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Timetable entry created", timetableService.createEntry(request)));
    }

    @GetMapping("/class/{classRoomId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get timetable for a classroom")
    public ResponseEntity<ApiResponse<List<TimetableResponse>>> getClassTimetable(
            @PathVariable Long classRoomId,
            @RequestParam String academicYear) {
        return ResponseEntity.ok(ApiResponse.success(timetableService.getClassTimetable(classRoomId, academicYear)));
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get timetable for a teacher")
    public ResponseEntity<ApiResponse<List<TimetableResponse>>> getTeacherTimetable(
            @PathVariable Long teacherId,
            @RequestParam String academicYear) {
        return ResponseEntity.ok(ApiResponse.success(timetableService.getTeacherTimetable(teacherId, academicYear)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a timetable entry")
    public ResponseEntity<ApiResponse<Void>> deleteEntry(@PathVariable Long id) {
        timetableService.deleteEntry(id);
        return ResponseEntity.ok(ApiResponse.success("Entry deleted", null));
    }
}
