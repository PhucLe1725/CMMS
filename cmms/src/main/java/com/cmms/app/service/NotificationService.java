package com.cmms.app.service;

import com.cmms.app.dto.notification.request.NotificationCreateRequest;
import com.cmms.app.dto.notification.request.NotificationUpdateRequest;
import com.cmms.app.dto.notification.response.NotificationResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.Notification;
import com.cmms.app.mapper.NotificationMapper;
import com.cmms.app.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationMapper notificationMapper;

    public NotificationResponse createNotification(NotificationCreateRequest request) {
        Notification entity = notificationMapper.toEntity(request);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setIsRead(false);
        Notification saved = notificationRepository.save(entity);
        return notificationMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public NotificationResponse getNotificationById(Long id) {
        Notification entity = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        return notificationMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<NotificationResponse> getAllNotifications() {
        List<Notification> entities = notificationRepository.findAll();
        List<NotificationResponse> responses = notificationMapper.toResponseList(entities);
        return BaseGetAllResponse.<NotificationResponse>builder().data(responses).build();
    }

    public NotificationResponse updateNotification(Long id, NotificationUpdateRequest request) {
        Notification entity = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notificationMapper.updateNotification(entity, request);
        Notification updated = notificationRepository.save(entity);
        return notificationMapper.toResponse(updated);
    }

    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<NotificationResponse> getByTenantAndUser(Long tenantId, Long userId) {
        List<Notification> entities = notificationRepository.findByTenantIdAndUserIdOrderByCreatedAtDesc(tenantId, userId);
        List<NotificationResponse> responses = notificationMapper.toResponseList(entities);
        return BaseGetAllResponse.<NotificationResponse>builder()
                .data(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<NotificationResponse> getUnreadByTenantAndUser(Long tenantId, Long userId) {
        List<Notification> entities = notificationRepository.findByTenantIdAndUserIdAndIsRead(tenantId, userId, false);
        List<NotificationResponse> responses = notificationMapper.toResponseList(entities);
        return BaseGetAllResponse.<NotificationResponse>builder()
                .data(responses)
                .build();
    }

    public NotificationResponse markAsRead(Long id) {
        Notification entity = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        entity.setIsRead(true);
        Notification updated = notificationRepository.save(entity);
        return notificationMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<NotificationResponse> getByWorkOrder(Long workOrderId) {
        List<Notification> entities = notificationRepository.findByWorkOrderId(workOrderId);
        List<NotificationResponse> responses = notificationMapper.toResponseList(entities);
        return BaseGetAllResponse.<NotificationResponse>builder()
                .data(responses)
                .build();
    }
}
