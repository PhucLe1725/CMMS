package com.cmms.app.mapper;


import com.cmms.app.dto.assetType.request.AssetTypeCreateRequest;
import com.cmms.app.dto.assetType.request.AssetTypeUpdateRequest;
import com.cmms.app.dto.assetType.response.AssetTypeResponse;
import com.cmms.app.entity.AssetType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AssetTypeMapper {
    AssetType toAssetType(AssetTypeCreateRequest assetTypeCreateRequest);

    AssetTypeResponse toAssetTypeResponse(AssetType assetType);

    void updateAssetType(@MappingTarget AssetType assetType, AssetTypeUpdateRequest assetTypeUpdateRequest);
}
