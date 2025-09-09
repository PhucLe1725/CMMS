package com.cmms.app.dto.location.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationCreateRequest {
    Long tenantId;
    String locationName;
    String description;
    Long parentLocationId;
}
