package com.cmms.app.dto.tagHistory.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagHistoryCreateRequest {
    Double tagValue1;
    Double tagValue2;
    String stringValue;
}
