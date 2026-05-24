package com.schoolerp.repository;

import com.schoolerp.entity.Timetable;
import com.schoolerp.enums.SchoolDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    List<Timetable> findByClassRoomIdAndAcademicYear(Long classRoomId, String academicYear);

    List<Timetable> findByClassRoomIdAndDayOfWeekAndAcademicYear(Long classRoomId, SchoolDay day, String academicYear);

    List<Timetable> findByTeacherIdAndAcademicYear(Long teacherId, String academicYear);
}
