package com.cmms.app.dto.user.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    Long userId;
    Long tenantId;
    String username;
    String password;
    String role;
    String email;
    String fullName;
    Integer isActive;
}
