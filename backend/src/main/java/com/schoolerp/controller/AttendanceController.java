package com.schoolerp.controller;

import com.schoolerp.dto.request.AttendanceRequest;
import com.schoolerp.dto.response.ApiResponse;
import com.schoolerp.dto.response.AttendanceResponse;
import com.schoolerp.security.UserPrincipal;
import com.schoolerp.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance", description = "Attendance tracking endpoints")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/mark")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Mark attendance for a class")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> markAttendance(
            @Valid @RequestBody AttendanceRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<AttendanceResponse> responses = attendanceService.markAttendance(request, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Attendance marked", responses));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
    @Operation(summary = "Get student attendance records")
    public ResponseEntity<ApiResponse<Page<AttendanceResponse>>> getStudentAttendance(
            @PathVariable Long studentId,
            @PageableDefault(size = 30) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getStudentAttendance(studentId, pageable)));
    }

    @GetMapping("/student/{studentId}/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
    @Operation(summary = "Get attendance summary for a student in a date range")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getAttendanceSummary(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceSummary(studentId, from, to)));
    }

    @GetMapping("/class/{classRoomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get attendance for a class on a specific date")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getClassAttendance(
            @PathVariable Long classRoomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getClassAttendance(classRoomId, date)));
    }
}
