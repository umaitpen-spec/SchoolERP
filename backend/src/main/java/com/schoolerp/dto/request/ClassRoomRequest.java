package com.schoolerp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClassRoomRequest {

    @NotBlank(message = "Class name is required")
    private String className;

    @NotBlank(message = "Section is required")
    private String section;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    private Integer capacity;
    private String roomNumber;
    private Long classTeacherId;
}
