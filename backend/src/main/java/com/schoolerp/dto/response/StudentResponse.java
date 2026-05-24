package com.schoolerp.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class StudentResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String profileImageUrl;
    private String rollNumber;
    private String admissionNumber;
    private LocalDate dateOfBirth;
    private String address;
    private String bloodGroup;
    private LocalDate admissionDate;
    private String phoneNumber;
    private String gender;
    private Long classRoomId;
    private String className;
    private String section;
    private Long parentId;
    private String parentName;
    private LocalDateTime createdAt;
}
