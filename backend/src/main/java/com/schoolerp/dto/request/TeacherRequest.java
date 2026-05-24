package com.schoolerp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeacherRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    private String employeeId;
    private String qualification;
    private String specialization;
    private Integer yearsOfExperience;
    private String address;
}
