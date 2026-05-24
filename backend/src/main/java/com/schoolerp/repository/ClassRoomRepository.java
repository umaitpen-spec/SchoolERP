package com.schoolerp.repository;

import com.schoolerp.entity.ClassRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {

    Page<ClassRoom> findByDeletedFalseAndAcademicYear(String academicYear, Pageable pageable);

    boolean existsByClassNameAndSectionAndAcademicYear(String className, String section, String academicYear);
}
