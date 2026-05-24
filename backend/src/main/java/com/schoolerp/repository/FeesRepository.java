package com.schoolerp.repository;

import com.schoolerp.entity.Fees;
import com.schoolerp.enums.FeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FeesRepository extends JpaRepository<Fees, Long> {

    Page<Fees> findByStudentIdAndAcademicYear(Long studentId, String academicYear, Pageable pageable);

    Page<Fees> findByStatus(FeeStatus status, Pageable pageable);

    List<Fees> findByStudentIdAndStatus(Long studentId, FeeStatus status);

    @Query("SELECT SUM(f.amount - f.amountPaid) FROM Fees f WHERE f.student.id = :studentId AND f.status != 'PAID'")
    BigDecimal findTotalOutstandingByStudent(Long studentId);
}
