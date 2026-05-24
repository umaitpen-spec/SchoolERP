package com.schoolerp.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TeacherResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String profileImageUrl;
    private String employeeId;
    private String qualification;
    private String specialization;
    private Integer yearsOfExperience;
    private String address;
    private String phoneNumber;
    private String gender;
    private List<String> subjectNames;
    private LocalDateTime createdAt;
}
