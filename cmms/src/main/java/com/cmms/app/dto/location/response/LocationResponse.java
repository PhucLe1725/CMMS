package com.cmms.app.dto.location.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationResponse {
    Long locationId;
    Long tenantId;
    String locationName;
    String description;
    Long parentLocationId;
}
