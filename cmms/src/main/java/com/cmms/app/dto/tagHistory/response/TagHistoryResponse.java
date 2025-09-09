package com.cmms.app.dto.tagHistory.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagHistoryResponse {
    Integer tagHistoryNdx;
    Double tagValue1;
    Double tagValue2;
    String stringValue;
    LocalDateTime tStamp;
}
