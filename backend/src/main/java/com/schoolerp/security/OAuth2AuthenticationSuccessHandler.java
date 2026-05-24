package com.schoolerp.security;

import com.schoolerp.dto.response.AuthResponse;
import com.schoolerp.dto.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Value("${app.oauth2.authorized-redirect-uris[0]}")
    private String defaultRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            String accessToken = tokenProvider.generateToken(userPrincipal);
            String refreshToken = tokenProvider.generateRefreshToken(userPrincipal);

            String redirectUri = request.getParameter("redirect_uri");

            if (StringUtils.hasText(redirectUri)) {
                String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                        .queryParam("token", accessToken)
                        .queryParam("refresh_token", refreshToken)
                        .build().toUriString();
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
                return;
            }

            if (StringUtils.hasText(defaultRedirectUri)) {
                String targetUrl = UriComponentsBuilder.fromUriString(defaultRedirectUri)
                        .queryParam("token", accessToken)
                        .queryParam("refresh_token", refreshToken)
                        .build().toUriString();
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
                return;
            }

            // Return JSON response for API clients
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(tokenProvider.getJwtExpirationMs() / 1000)
                    .build();
            objectMapper.writeValue(response.getWriter(), authResponse);
        } catch (Exception ex) {
            log.error("OAuth2 success handler failed", ex);

            // Best-effort graceful response so client doesn't see generic 500
            String redirectUri = request.getParameter("redirect_uri");
            String targetMessage = ex.getMessage() != null ? ex.getMessage() : "OAuth2 success handler error";

            if (StringUtils.hasText(redirectUri)) {
                String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                        .queryParam("error", "oauth2_success_failed")
                        .queryParam("message", targetMessage)
                        .build().toUriString();
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
                return;
            }

            if (StringUtils.hasText(defaultRedirectUri)) {
                String targetUrl = UriComponentsBuilder.fromUriString(defaultRedirectUri)
                        .queryParam("error", "oauth2_success_failed")
                        .queryParam("message", targetMessage)
                        .build().toUriString();
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
                return;
            }

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(),
                    com.schoolerp.dto.response.ApiResponse.error(
                            "OAuth2 authentication succeeded but token handling failed: " + targetMessage));
        }
    }


}
