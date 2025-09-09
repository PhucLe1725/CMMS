package com.cmms.app.mapper;

import com.cmms.app.dto.asset.request.AssetCreateRequest;
import com.cmms.app.dto.asset.request.AssetUpdateRequest;
import com.cmms.app.dto.asset.response.AssetResponse;
import com.cmms.app.entity.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    @Mapping(target = "assetId", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Asset toAsset(AssetCreateRequest assetCreateRequest);

    AssetResponse toAssetResponse(Asset asset);

    @Mapping(target = "assetId", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateAsset(@MappingTarget Asset asset, AssetUpdateRequest request);
}
