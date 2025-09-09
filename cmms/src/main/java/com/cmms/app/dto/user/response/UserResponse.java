package com.cmms.app.dto.user.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long userId;
    Long tenantId;
    String username;
    String role;
    String email;
    String fullName;
    Integer isActive;
    OffsetDateTime createdAt;
}
