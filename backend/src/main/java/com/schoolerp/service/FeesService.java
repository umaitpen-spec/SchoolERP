package com.schoolerp.service;

import com.schoolerp.dto.request.FeesRequest;
import com.schoolerp.dto.response.FeesResponse;
import com.schoolerp.entity.Fees;
import com.schoolerp.entity.Student;
import com.schoolerp.enums.FeeStatus;
import com.schoolerp.exception.BadRequestException;
import com.schoolerp.exception.ResourceNotFoundException;
import com.schoolerp.repository.FeesRepository;
import com.schoolerp.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeesService {

    private final FeesRepository feesRepository;
    private final StudentRepository studentRepository;

    public FeesResponse createFeeRecord(FeesRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));

        Fees fees = Fees.builder()
                .student(student)
                .feeType(request.getFeeType())
                .amount(request.getAmount())
                .amountPaid(BigDecimal.ZERO)
                .dueDate(request.getDueDate())
                .status(FeeStatus.PENDING)
                .academicYear(request.getAcademicYear())
                .term(request.getTerm())
                .remarks(request.getRemarks())
                .build();

        return mapToResponse(feesRepository.save(fees));
    }

    @Transactional(readOnly = true)
    public Page<FeesResponse> getStudentFees(Long studentId, String academicYear, Pageable pageable) {
        return feesRepository.findByStudentIdAndAcademicYear(studentId, academicYear, pageable).map(this::mapToResponse);
    }

    public FeesResponse recordPayment(Long feeId, BigDecimal paymentAmount, String transactionId) {
        Fees fees = feesRepository.findById(feeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fees", "id", feeId));

        if (fees.getStatus() == FeeStatus.PAID) {
            throw new BadRequestException("Fee is already fully paid");
        }

        BigDecimal newAmountPaid = fees.getAmountPaid().add(paymentAmount);
        if (newAmountPaid.compareTo(fees.getAmount()) > 0) {
            throw new BadRequestException("Payment amount exceeds fee amount");
        }

        fees.setAmountPaid(newAmountPaid);
        fees.setTransactionId(transactionId);
        fees.setPaidDate(LocalDate.now());

        if (newAmountPaid.compareTo(fees.getAmount()) == 0) {
            fees.setStatus(FeeStatus.PAID);
        } else {
            fees.setStatus(FeeStatus.PARTIAL);
        }

        return mapToResponse(feesRepository.save(fees));
    }

    private FeesResponse mapToResponse(Fees f) {
        BigDecimal balance = f.getAmount().subtract(f.getAmountPaid());
        return FeesResponse.builder()
                .id(f.getId())
                .studentId(f.getStudent().getId())
                .studentName(f.getStudent().getUser().getFullName())
                .rollNumber(f.getStudent().getRollNumber())
                .feeType(f.getFeeType())
                .amount(f.getAmount())
                .amountPaid(f.getAmountPaid())
                .balance(balance)
                .dueDate(f.getDueDate())
                .paidDate(f.getPaidDate())
                .status(f.getStatus())
                .academicYear(f.getAcademicYear())
                .term(f.getTerm())
                .transactionId(f.getTransactionId())
                .remarks(f.getRemarks())
                .createdAt(f.getCreatedAt())
                .build();
    }
}
