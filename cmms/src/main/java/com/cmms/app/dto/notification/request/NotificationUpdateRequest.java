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
public class NotificationUpdateRequest {
    String title;
    String message;
    String type;
    Boolean isRead;
}
