package com.cmms.app.dto.notification.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    Long notificationId;
    Long tenantId;
    Long userId;
    Long workOrderId;
    Long assetId;
    String title;
    String message;
    String type;
    Boolean isRead;
    OffsetDateTime createdAt;
}
