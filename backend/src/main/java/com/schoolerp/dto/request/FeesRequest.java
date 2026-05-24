package com.schoolerp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FeesRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotBlank(message = "Fee type is required")
    private String feeType;

    @NotNull
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    private String term;
    private String remarks;
}
