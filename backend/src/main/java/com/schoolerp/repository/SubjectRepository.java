package com.schoolerp.repository;

import com.schoolerp.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Page<Subject> findByDeletedFalse(Pageable pageable);

    boolean existsBySubjectCode(String subjectCode);
}
