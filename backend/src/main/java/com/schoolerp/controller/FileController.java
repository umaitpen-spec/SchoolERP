package com.schoolerp.controller;

import com.schoolerp.dto.response.ApiResponse;
import com.schoolerp.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "File upload endpoints")
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Upload profile image")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadProfileImage(
            @RequestParam("file") MultipartFile file) {
        String url = fileStorageService.storeFile(file, "profiles");
        return ResponseEntity.ok(ApiResponse.success(Map.of("url", url)));
    }

    @PostMapping("/upload/document")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Upload a document")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadDocument(
            @RequestParam("file") MultipartFile file) {
        String url = fileStorageService.storeFile(file, "documents");
        return ResponseEntity.ok(ApiResponse.success(Map.of("url", url)));
    }
}
