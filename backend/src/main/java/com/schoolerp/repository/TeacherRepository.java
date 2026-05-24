package com.schoolerp.repository;

import com.schoolerp.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUserId(Long userId);

    Optional<Teacher> findByEmployeeId(String employeeId);

    Page<Teacher> findByDeletedFalse(Pageable pageable);

    @Query("SELECT t FROM Teacher t WHERE t.deleted = false AND " +
           "(LOWER(t.user.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.employeeId) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Teacher> searchTeachers(String query, Pageable pageable);

    long countByDeletedFalse();
}
