package com.schoolerp.service;

import com.schoolerp.dto.request.NotificationRequest;
import com.schoolerp.dto.response.NotificationResponse;
import com.schoolerp.entity.Notification;
import com.schoolerp.entity.User;
import com.schoolerp.exception.ResourceNotFoundException;
import com.schoolerp.repository.NotificationRepository;
import com.schoolerp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void sendNotification(NotificationRequest request) {
        List<User> recipients;
        if (request.getRecipientUserIds() != null && !request.getRecipientUserIds().isEmpty()) {
            recipients = userRepository.findAllById(request.getRecipientUserIds());
        } else {
            recipients = userRepository.findAll();
        }

        List<Notification> notifications = recipients.stream().map(user ->
                Notification.builder()
                        .recipient(user)
                        .title(request.getTitle())
                        .message(request.getMessage())
                        .type(request.getType())
                        .referenceId(request.getReferenceId())
                        .build()
        ).collect(Collectors.toList());

        notificationRepository.saveAll(notifications);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByRecipientIdAndReadFalse(userId);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    private NotificationResponse mapToResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .type(n.getType())
                .read(n.isRead())
                .referenceId(n.getReferenceId())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
