package com.cmms.app.dto.workOrder.request;

import com.cmms.app.dto.base.request.BaseGetAllRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkOrderGetAllRequest extends BaseGetAllRequest {
    private Long tenantId;
}
