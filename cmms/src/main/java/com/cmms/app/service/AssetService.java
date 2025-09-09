package com.cmms.app.service;

import com.cmms.app.dto.asset.request.AssetCreateRequest;
import com.cmms.app.dto.asset.request.AssetUpdateRequest;
import com.cmms.app.dto.asset.response.AssetResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.Asset;
import com.cmms.app.mapper.AssetMapper;
import com.cmms.app.repository.AssetRepository;
import com.cmms.app.repository.AssetTypeRepository;
import com.cmms.app.repository.LocationRepository;
import com.cmms.app.repository.TenantRepository;
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
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private AssetTypeRepository assetTypeRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private AssetMapper assetMapper;

    public AssetResponse createAsset(AssetCreateRequest request, long tenantId) {
        Asset asset = assetMapper.toAsset(request);
        asset.setTenantId(tenantId);

        asset = assetRepository.save(asset);
        return assetMapper.toAssetResponse(asset);
    }

    public AssetResponse getAssetById(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        return assetMapper.toAssetResponse(asset);
    }

    public BaseGetAllResponse<AssetResponse> getAllAssetsByTenantId(Long tenantId, Integer skipCount, Integer maxResultCount, String sortBy, String sortDirection) {
        List<Asset> assets = assetRepository.findAllAssetsByTenantIdWithSortAndLimit(tenantId, skipCount, maxResultCount, sortBy, sortDirection);
        long totalRecords = assetRepository.countByTenantId(tenantId);
        
        List<AssetResponse> assetResponses = assets
                .stream()
                .map(assetMapper::toAssetResponse)
                .toList();

        return BaseGetAllResponse.<AssetResponse>builder()
                .data(assetResponses)
                .totalRecords(totalRecords)
                .build();
    }

    public AssetResponse updateAsset(AssetUpdateRequest request) {
        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        assetMapper.updateAsset(asset, request);
        asset = assetRepository.save(asset);
        return assetMapper.toAssetResponse(asset);
    }

    public void deleteAsset(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        assetRepository.delete(asset);
    }
}
