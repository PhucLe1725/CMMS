package com.cmms.app.dto.inventory.request;

import com.cmms.app.dto.base.request.BaseGetAllRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryGetAllRequest extends BaseGetAllRequest {
    private Long tenantId;
}
