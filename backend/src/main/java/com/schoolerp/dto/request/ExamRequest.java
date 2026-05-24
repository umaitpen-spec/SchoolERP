package com.schoolerp.dto.request;

import com.schoolerp.enums.ExamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ExamRequest {

    @NotBlank(message = "Exam name is required")
    private String examName;

    @NotNull(message = "Exam type is required")
    private ExamType examType;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Classroom ID is required")
    private Long classRoomId;

    @NotNull(message = "Exam date is required")
    private LocalDate examDate;

    private LocalTime startTime;
    private LocalTime endTime;

    @NotNull
    @Positive(message = "Total marks must be positive")
    private Double totalMarks;

    @NotNull
    @Positive(message = "Passing marks must be positive")
    private Double passingMarks;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    private String description;
}
