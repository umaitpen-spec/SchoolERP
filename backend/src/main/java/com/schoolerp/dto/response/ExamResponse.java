package com.schoolerp.dto.response;

import com.schoolerp.enums.ExamType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ExamResponse {
    private Long id;
    private String examName;
    private ExamType examType;
    private Long subjectId;
    private String subjectName;
    private Long classRoomId;
    private String className;
    private LocalDate examDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double totalMarks;
    private Double passingMarks;
    private String academicYear;
    private String description;
}
