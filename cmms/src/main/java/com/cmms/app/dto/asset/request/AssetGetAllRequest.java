package com.cmms.app.dto.asset.request;

import com.cmms.app.dto.base.request.BaseGetAllRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetGetAllRequest extends BaseGetAllRequest {
    private Long tenantId;
}
