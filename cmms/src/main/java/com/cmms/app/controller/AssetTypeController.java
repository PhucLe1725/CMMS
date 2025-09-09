package com.cmms.app.controller;

import com.cmms.app.dto.assetType.request.AssetTypeCreateRequest;
import com.cmms.app.dto.assetType.request.AssetTypeUpdateRequest;
import com.cmms.app.dto.assetType.response.AssetTypeResponse;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.AssetType;
import com.cmms.app.repository.AssetRepository;
import com.cmms.app.service.AssetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset-types")
public class AssetTypeController {
    @Autowired
    AssetRepository assetRepository;

    @Autowired
    AssetTypeService assetTypeService;

    @PostMapping("/create")
    public ApiResponse<AssetTypeResponse> createAssetType(@RequestBody AssetTypeCreateRequest assetTypeCreateRequest) {
        return ApiResponse.<AssetTypeResponse>builder()
                .result(assetTypeService.createAssetType(assetTypeCreateRequest))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<BaseGetAllResponse<AssetTypeResponse>> getAllAssetTypes() {
        return ApiResponse.<BaseGetAllResponse<AssetTypeResponse>>builder()
                .result(assetTypeService.getAllAssetTypes())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AssetTypeResponse> getAssetTypeById(@PathVariable Long id) {
        return ApiResponse.<AssetTypeResponse>builder()
                .result(assetTypeService.getAssetTypeById(id))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<AssetTypeResponse> updateAssetType(@PathVariable Long id, @RequestBody AssetTypeUpdateRequest assetTypeUpdateRequest) {
        assetTypeUpdateRequest.setAssetTypeId(id);
        return ApiResponse.<AssetTypeResponse>builder()
                .result(assetTypeService.updateAssetType(assetTypeUpdateRequest))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteAssetType(@PathVariable Long id) {
        assetTypeService.deleteAssetType(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }
}
