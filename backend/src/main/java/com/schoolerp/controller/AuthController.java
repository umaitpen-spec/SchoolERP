package com.schoolerp.controller;

import com.schoolerp.dto.response.ApiResponse;
import com.schoolerp.dto.response.AuthResponse;
import com.schoolerp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "OAuth2 and JWT auth endpoints")
public class AuthController {

    private final UserService userService;

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT access token using refresh token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestParam String refreshToken) {
        AuthResponse response = userService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", response));
    }

    @GetMapping("/oauth2/callback/google")
    @Operation(summary = "Google OAuth2 callback - handled by Spring Security, documented here for reference")
    public ResponseEntity<String> oauthCallback() {
        return ResponseEntity.ok("OAuth2 flow handled by Spring Security");
    }
}
