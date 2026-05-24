package com.schoolerp.service;

import com.schoolerp.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file, String subDirectory) {
        if (file.isEmpty()) {
            throw new BadRequestException("Cannot store empty file");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String filename = UUID.randomUUID() + extension;

        try {
            Path uploadPath = Paths.get(uploadDir).resolve(subDirectory);
            Files.createDirectories(uploadPath);
            Path targetPath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + subDirectory + "/" + filename;
        } catch (IOException ex) {
            log.error("Failed to store file: {}", ex.getMessage());
            throw new RuntimeException("Failed to store file", ex);
        }
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null) return;
        String relativePath = fileUrl.replace("/uploads/", "");
        Path filePath = Paths.get(uploadDir).resolve(relativePath);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            log.warn("Failed to delete file: {}", fileUrl);
        }
    }
}
