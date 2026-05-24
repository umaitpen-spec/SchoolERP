package com.schoolerp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MarksRequest {

    @NotNull(message = "Exam ID is required")
    private Long examId;

    @NotNull(message = "Marks records are required")
    private List<MarksRecord> records;

    @Data
    public static class MarksRecord {
        @NotNull
        private Long studentId;
        private Double marksObtained;
        private String grade;
        private String remarks;
        private boolean absent;
    }
}
