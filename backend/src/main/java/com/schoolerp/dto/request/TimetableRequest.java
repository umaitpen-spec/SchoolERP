package com.schoolerp.dto.request;

import com.schoolerp.enums.SchoolDay;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimetableRequest {

    @NotNull(message = "Classroom ID is required")
    private Long classRoomId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotNull(message = "Day of week is required")
    private SchoolDay dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    private String roomNumber;
}
