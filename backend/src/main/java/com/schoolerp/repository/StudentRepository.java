package com.schoolerp.repository;

import com.schoolerp.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRollNumber(String rollNumber);

    Optional<Student> findByAdmissionNumber(String admissionNumber);

    Optional<Student> findByUserId(Long userId);

    Page<Student> findByDeletedFalseAndClassRoomId(Long classRoomId, Pageable pageable);

    Page<Student> findByDeletedFalse(Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.deleted = false AND " +
           "(LOWER(s.user.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.admissionNumber) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Student> searchStudents(String query, Pageable pageable);

    long countByDeletedFalse();

    long countByClassRoomIdAndDeletedFalse(Long classRoomId);
}
