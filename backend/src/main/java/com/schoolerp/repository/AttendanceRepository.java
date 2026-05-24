package com.schoolerp.repository;

import com.schoolerp.entity.Attendance;
import com.schoolerp.enums.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudentIdAndAttendanceDateBetween(Long studentId, LocalDate from, LocalDate to);

    List<Attendance> findByClassRoomIdAndAttendanceDate(Long classRoomId, LocalDate date);

    boolean existsByStudentIdAndAttendanceDateAndSubjectId(Long studentId, LocalDate date, Long subjectId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId " +
           "AND a.status = :status AND a.attendanceDate BETWEEN :from AND :to")
    long countByStudentAndStatusAndDateRange(Long studentId, AttendanceStatus status, LocalDate from, LocalDate to);

    Page<Attendance> findByStudentId(Long studentId, Pageable pageable);

    Page<Attendance> findByClassRoomIdAndAttendanceDateBetween(Long classRoomId, LocalDate from, LocalDate to, Pageable pageable);
}
