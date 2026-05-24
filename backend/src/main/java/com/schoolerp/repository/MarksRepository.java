package com.schoolerp.repository;

import com.schoolerp.entity.Marks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {

    Optional<Marks> findByStudentIdAndExamId(Long studentId, Long examId);

    List<Marks> findByExamId(Long examId);

    Page<Marks> findByStudentId(Long studentId, Pageable pageable);

    boolean existsByStudentIdAndExamId(Long studentId, Long examId);
}
