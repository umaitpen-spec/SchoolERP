package com.schoolerp.controller;

import com.schoolerp.dto.request.ExamRequest;
import com.schoolerp.dto.request.MarksRequest;
import com.schoolerp.dto.response.ApiResponse;
import com.schoolerp.dto.response.ExamResponse;
import com.schoolerp.dto.response.MarksResponse;
import com.schoolerp.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exams")
@RequiredArgsConstructor
@Tag(name = "Exams & Marks", description = "Exam and marks management endpoints")
public class ExamController {

    private final ExamService examService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Create an exam")
    public ResponseEntity<ApiResponse<ExamResponse>> createExam(@Valid @RequestBody ExamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Exam created", examService.createExam(request)));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all exams with pagination")
    public ResponseEntity<ApiResponse<Page<ExamResponse>>> getExams(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(examService.getExams(pageable)));
    }

    @PostMapping("/marks")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Enter marks for an exam")
    public ResponseEntity<ApiResponse<List<MarksResponse>>> enterMarks(@Valid @RequestBody MarksRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Marks entered", examService.enterMarks(request)));
    }

    @GetMapping("/marks/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
    @Operation(summary = "Get marks for a student")
    public ResponseEntity<ApiResponse<Page<MarksResponse>>> getStudentMarks(
            @PathVariable Long studentId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(examService.getStudentMarks(studentId, pageable)));
    }
}
