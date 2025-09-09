package com.cmms.app.dto.notification.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationCreateRequest {
    Long tenantId;
    Long userId;
    Long workOrderId;
    Long assetId;
    String title;
    String message;
    String type;
}
