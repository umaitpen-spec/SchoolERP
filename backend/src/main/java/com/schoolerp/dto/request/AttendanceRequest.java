package com.schoolerp.dto.request;

import com.schoolerp.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AttendanceRequest {

    @NotNull(message = "Class room ID is required")
    private Long classRoomId;

    private Long subjectId;

    @NotNull(message = "Attendance date is required")
    private LocalDate attendanceDate;

    @NotNull(message = "Attendance records are required")
    private List<AttendanceRecord> records;

    @Data
    public static class AttendanceRecord {
        @NotNull
        private Long studentId;
        @NotNull
        private AttendanceStatus status;
        private String remarks;
    }
}
