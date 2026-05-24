package com.schoolerp.controller;

import com.schoolerp.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GlobalErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ApiResponse<Void>> handleError(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        String message = getErrorMessage(request, status);
        
        log.error("Error occurred: status={}, message={}, path={}", 
                status.value(), message, request.getRequestURI());
        
        return ResponseEntity.status(status)
                .body(ApiResponse.error(message));
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (IllegalArgumentException e) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String getErrorMessage(HttpServletRequest request, HttpStatus status) {
        String message = (String) request.getAttribute("javax.servlet.error.message");
        Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
        
        if (exception != null) {
            log.error("Exception during request:", exception);
            return exception.getMessage() != null ? exception.getMessage() : status.getReasonPhrase();
        }
        
        return message != null ? message : status.getReasonPhrase();
    }
}
