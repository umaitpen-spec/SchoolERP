package com.schoolerp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MarksResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private Long examId;
    private String examName;
    private String subjectName;
    private Double marksObtained;
    private Double totalMarks;
    private Double percentage;
    private String grade;
    private String remarks;
    private boolean absent;
}
