package com.schoolerp.security;

import com.schoolerp.entity.User;
import com.schoolerp.enums.Role;
import com.schoolerp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String googleId = (String) attributes.get("sub");
        String picture = (String) attributes.get("picture");

        User user = userRepository.findByEmail(email)
                .map(existingUser -> updateExistingUser(existingUser, name, picture))
                .orElseGet(() -> registerNewUser(email, name, googleId, picture));

        return UserPrincipal.create(user, attributes);
    }

    private User registerNewUser(String email, String name, String googleId, String picture) {
        log.info("Registering new user with email: {}", email);
        User user = User.builder()
                .email(email)
                .fullName(name)
                .googleId(googleId)
                .profileImageUrl(picture)
                .role(Role.STUDENT)  // default role; admin can change later
                .active(true)
                .lastLogin(LocalDateTime.now())
                .build();
        return userRepository.save(user);
    }

    private User updateExistingUser(User user, String name, String picture) {
        user.setFullName(name);
        user.setProfileImageUrl(picture);
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }
}
