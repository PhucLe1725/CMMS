package com.cmms.app.mapper;

import com.cmms.app.dto.notification.request.NotificationCreateRequest;
import com.cmms.app.dto.notification.request.NotificationUpdateRequest;
import com.cmms.app.dto.notification.response.NotificationResponse;
import com.cmms.app.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isRead", ignore = true)
    Notification toEntity(NotificationCreateRequest request);

    NotificationResponse toResponse(Notification entity);

    List<NotificationResponse> toResponseList(List<Notification> entities);

    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "workOrderId", ignore = true)
    @Mapping(target = "assetId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateNotification(@MappingTarget Notification entity, NotificationUpdateRequest request);
}
