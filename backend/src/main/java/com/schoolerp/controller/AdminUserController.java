package com.schoolerp.controller;

import com.schoolerp.dto.response.ApiResponse;
import com.schoolerp.dto.response.UserResponse;
import com.schoolerp.enums.Role;
import com.schoolerp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - Users", description = "Admin user management endpoints")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "List all users by role")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsersByRole(
            @RequestParam(required = false) Role role,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUsersByRole(role, pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @PatchMapping("/{id}/role")
    @Operation(summary = "Update user role")
    public ResponseEntity<ApiResponse<UserResponse>> updateRole(
            @PathVariable Long id,
            @RequestParam Role role) {
        return ResponseEntity.ok(ApiResponse.success("Role updated", userService.updateRole(id, role)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a user")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.softDelete(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted", null));
    }
}
