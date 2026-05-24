package com.schoolerp.controller;

import com.schoolerp.dto.request.FeesRequest;
import com.schoolerp.dto.response.ApiResponse;
import com.schoolerp.dto.response.FeesResponse;
import com.schoolerp.service.FeesService;
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

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/fees")
@RequiredArgsConstructor
@Tag(name = "Fees", description = "Fee management endpoints")
public class FeesController {

    private final FeesService feesService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a fee record for a student")
    public ResponseEntity<ApiResponse<FeesResponse>> createFee(@Valid @RequestBody FeesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Fee record created", feesService.createFeeRecord(request)));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'PARENT')")
    @Operation(summary = "Get all fees for a student")
    public ResponseEntity<ApiResponse<Page<FeesResponse>>> getStudentFees(
            @PathVariable Long studentId,
            @RequestParam String academicYear,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(feesService.getStudentFees(studentId, academicYear, pageable)));
    }

    @PostMapping("/{feeId}/pay")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Record a payment for a fee")
    public ResponseEntity<ApiResponse<FeesResponse>> recordPayment(
            @PathVariable Long feeId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String transactionId) {
        return ResponseEntity.ok(ApiResponse.success("Payment recorded", feesService.recordPayment(feeId, amount, transactionId)));
    }
}
