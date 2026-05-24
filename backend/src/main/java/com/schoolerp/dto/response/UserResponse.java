package com.schoolerp.dto.response;

import com.schoolerp.enums.Gender;
import com.schoolerp.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String profileImageUrl;
    private Role role;
    private Gender gender;
    private String phoneNumber;
    private boolean active;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
}
