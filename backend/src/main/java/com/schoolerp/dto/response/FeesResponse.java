package com.schoolerp.dto.response;

import com.schoolerp.enums.FeeStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class FeesResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private String feeType;
    private BigDecimal amount;
    private BigDecimal amountPaid;
    private BigDecimal balance;
    private LocalDate dueDate;
    private LocalDate paidDate;
    private FeeStatus status;
    private String academicYear;
    private String term;
    private String transactionId;
    private String remarks;
    private LocalDateTime createdAt;
}
