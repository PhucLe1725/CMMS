package com.cmms.app.service;

import com.cmms.app.dto.asset.response.AssetResponse;
import com.cmms.app.dto.assetType.request.AssetTypeCreateRequest;
import com.cmms.app.dto.assetType.request.AssetTypeUpdateRequest;
import com.cmms.app.dto.assetType.response.AssetTypeResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.AssetType;
import com.cmms.app.mapper.AssetTypeMapper;
import com.cmms.app.repository.AssetTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class AssetTypeService {

    @Autowired
    private AssetTypeRepository assetTypeRepository;
    @Autowired
    private AssetTypeMapper assetTypeMapper;

    public AssetTypeResponse createAssetType(AssetTypeCreateRequest request) {
        AssetType assetType = assetTypeMapper.toAssetType(request);
        assetType = assetTypeRepository.save(assetType);
        return assetTypeMapper.toAssetTypeResponse(assetType);
    }

    public AssetTypeResponse getAssetTypeById(Long assetTypeId) {
        AssetType assetType = assetTypeRepository.findById(assetTypeId)
                .orElseThrow(() -> new RuntimeException("Asset Type not found"));
        return assetTypeMapper.toAssetTypeResponse(assetType);
    }

    public BaseGetAllResponse<AssetTypeResponse> getAllAssetTypes() {

        List<AssetTypeResponse> assetTypeResponses = assetTypeRepository.findAllAssetType()
                .stream()
                .map(assetTypeMapper::toAssetTypeResponse)
                .toList();

        return BaseGetAllResponse.<AssetTypeResponse>builder()
                .data(assetTypeResponses)
                .build();
    }

    public AssetTypeResponse updateAssetType(AssetTypeUpdateRequest request) {
        AssetType assetType = assetTypeRepository.findById(request.getAssetTypeId())
                .orElseThrow(() -> new RuntimeException("Asset Type not found"));
        assetTypeMapper.updateAssetType(assetType, request);
        assetType = assetTypeRepository.save(assetType);
        return assetTypeMapper.toAssetTypeResponse(assetType);
    }


    public void deleteAssetType(Long assetTypeId) {
        AssetType assetType = assetTypeRepository.findById(assetTypeId)
                .orElseThrow(() -> new RuntimeException("Asset Type not found"));
        assetTypeRepository.delete(assetType);
    }

}
