package com.schoolerp.security;

import com.schoolerp.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.error("OAuth2 authentication failed", exception);

        String errorMessage = exception.getMessage();
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oAuth2Ex = (OAuth2AuthenticationException) exception;
            errorMessage = oAuth2Ex.getError().getErrorCode() + ": " + oAuth2Ex.getError().getDescription();
        }

        String redirectUri = request.getParameter("redirect_uri");
        
        if (redirectUri != null && !redirectUri.isEmpty()) {
            // Redirect with error details
            String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                    .queryParam("error", "authentication_failed")
                    .queryParam("message", errorMessage)
                    .build().toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // Return JSON error response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            
            ApiResponse<?> errorResponse = ApiResponse.builder()
                    .success(false)
                    .message("OAuth2 authentication failed: " + errorMessage)
                    .build();
            
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }
    }
}
