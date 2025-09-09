package com.cmms.app.controller;

import com.cmms.app.dto.asset.request.AssetCreateRequest;
import com.cmms.app.dto.asset.request.AssetGetAllRequest;
import com.cmms.app.dto.asset.request.AssetUpdateRequest;
import com.cmms.app.dto.asset.response.AssetResponse;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.repository.AssetRepository;
import com.cmms.app.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetService assetService;


    @PostMapping("/create")
    public ApiResponse<AssetResponse> createAsset(@RequestBody AssetCreateRequest assetCreateRequest, @RequestParam Long tenantId) {
        return ApiResponse.<AssetResponse>builder()
                .result(assetService.createAsset(assetCreateRequest, tenantId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AssetResponse> getAssetById(@PathVariable Long id) {
        return ApiResponse.<AssetResponse>builder()
                .result(assetService.getAssetById(id))
                .build();
    }

    @GetMapping("/getAllByTenantId")
    public ApiResponse<BaseGetAllResponse<AssetResponse>> getAllAssetsByTenantId(
            @RequestParam Long tenantId,
            @RequestParam(defaultValue = "0") Integer skipCount,
            @RequestParam(defaultValue = "10") Integer maxResultCount,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        AssetGetAllRequest request = new AssetGetAllRequest();
        request.setTenantId(tenantId);
        request.setSkipCount(skipCount);
        request.setMaxResultCount(maxResultCount);
        request.setSortBy(sortBy);
        request.setSortDirection(sortDirection);

        return ApiResponse.<BaseGetAllResponse<AssetResponse>>builder()
                .result(assetService.getAllAssetsByTenantId(request.getTenantId(), request.getSkipCount(), request.getMaxResultCount(), request.getSortBy(), request.getSortDirection()))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<AssetResponse> updateAsset(@PathVariable Long id, @RequestBody AssetUpdateRequest assetUpdateRequest) {
        assetUpdateRequest.setAssetId(id);
        return ApiResponse.<AssetResponse>builder()
                .result(assetService.updateAsset(assetUpdateRequest))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }
}