package com.schoolerp.dto.response;

import com.schoolerp.enums.SchoolDay;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class TimetableResponse {
    private Long id;
    private Long classRoomId;
    private String className;
    private String section;
    private Long subjectId;
    private String subjectName;
    private Long teacherId;
    private String teacherName;
    private SchoolDay dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String academicYear;
    private String roomNumber;
}
