package com.schoolerp.dto.request;

import com.schoolerp.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Roll number is required")
    private String rollNumber;

    @NotBlank(message = "Admission number is required")
    private String admissionNumber;

    private LocalDate dateOfBirth;
    private String address;
    private String bloodGroup;
    private LocalDate admissionDate;
    private Long classRoomId;
    private Long parentId;
}
