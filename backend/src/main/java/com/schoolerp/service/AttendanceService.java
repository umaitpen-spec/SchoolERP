package com.schoolerp.service;

import com.schoolerp.dto.request.AttendanceRequest;
import com.schoolerp.dto.response.AttendanceResponse;
import com.schoolerp.entity.Attendance;
import com.schoolerp.entity.ClassRoom;
import com.schoolerp.entity.Student;
import com.schoolerp.entity.Subject;
import com.schoolerp.entity.Teacher;
import com.schoolerp.enums.AttendanceStatus;
import com.schoolerp.exception.ResourceNotFoundException;
import com.schoolerp.repository.AttendanceRepository;
import com.schoolerp.repository.ClassRoomRepository;
import com.schoolerp.repository.StudentRepository;
import com.schoolerp.repository.SubjectRepository;
import com.schoolerp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    public List<AttendanceResponse> markAttendance(AttendanceRequest request, Long teacherUserId) {
        ClassRoom classRoom = classRoomRepository.findById(request.getClassRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("ClassRoom", "id", request.getClassRoomId()));

        Subject subject = null;
        if (request.getSubjectId() != null) {
            subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", request.getSubjectId()));
        }

        Teacher teacher = teacherRepository.findByUserId(teacherUserId).orElse(null);

        Subject finalSubject = subject;
        Teacher finalTeacher = teacher;

        return request.getRecords().stream().map(record -> {
            Student student = studentRepository.findById(record.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", record.getStudentId()));

            Attendance attendance = Attendance.builder()
                    .student(student)
                    .classRoom(classRoom)
                    .subject(finalSubject)
                    .teacher(finalTeacher)
                    .attendanceDate(request.getAttendanceDate())
                    .status(record.getStatus())
                    .remarks(record.getRemarks())
                    .build();

            return mapToResponse(attendanceRepository.save(attendance));
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<AttendanceResponse> getStudentAttendance(Long studentId, Pageable pageable) {
        return attendanceRepository.findByStudentId(studentId, pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getAttendanceSummary(Long studentId, LocalDate from, LocalDate to) {
        return Map.of(
            "present", attendanceRepository.countByStudentAndStatusAndDateRange(studentId, AttendanceStatus.PRESENT, from, to),
            "absent", attendanceRepository.countByStudentAndStatusAndDateRange(studentId, AttendanceStatus.ABSENT, from, to),
            "late", attendanceRepository.countByStudentAndStatusAndDateRange(studentId, AttendanceStatus.LATE, from, to),
            "excused", attendanceRepository.countByStudentAndStatusAndDateRange(studentId, AttendanceStatus.EXCUSED, from, to)
        );
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getClassAttendance(Long classRoomId, LocalDate date) {
        return attendanceRepository.findByClassRoomIdAndAttendanceDate(classRoomId, date)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private AttendanceResponse mapToResponse(Attendance a) {
        return AttendanceResponse.builder()
                .id(a.getId())
                .studentId(a.getStudent().getId())
                .studentName(a.getStudent().getUser().getFullName())
                .rollNumber(a.getStudent().getRollNumber())
                .classRoomId(a.getClassRoom() != null ? a.getClassRoom().getId() : null)
                .className(a.getClassRoom() != null ? a.getClassRoom().getClassName() : null)
                .subjectId(a.getSubject() != null ? a.getSubject().getId() : null)
                .subjectName(a.getSubject() != null ? a.getSubject().getSubjectName() : null)
                .teacherId(a.getTeacher() != null ? a.getTeacher().getId() : null)
                .teacherName(a.getTeacher() != null ? a.getTeacher().getUser().getFullName() : null)
                .attendanceDate(a.getAttendanceDate())
                .status(a.getStatus())
                .remarks(a.getRemarks())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
