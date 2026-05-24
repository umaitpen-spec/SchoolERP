package com.schoolerp.dto.response;

import com.schoolerp.enums.AttendanceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class AttendanceResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private Long classRoomId;
    private String className;
    private Long subjectId;
    private String subjectName;
    private Long teacherId;
    private String teacherName;
    private LocalDate attendanceDate;
    private AttendanceStatus status;
    private String remarks;
    private LocalDateTime createdAt;
}
