package com.cmms.app.dto.user.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserGetAllRequest {
    Long userId;
    Long tenantId;
    String username;
    String role;
    String email;
    String fullName;
    Integer isActive;
    OffsetDateTime createdAt;
}
