package com.schoolerp.repository;

import com.schoolerp.entity.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    Page<Exam> findByClassRoomIdAndAcademicYear(Long classRoomId, String academicYear, Pageable pageable);

    Page<Exam> findBySubjectIdAndAcademicYear(Long subjectId, String academicYear, Pageable pageable);

    Page<Exam> findByDeletedFalse(Pageable pageable);
}
