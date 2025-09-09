package com.cmms.app.controller;

import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.dto.location.request.LocationCreateRequest;
import com.cmms.app.dto.location.request.LocationUpdateRequest;
import com.cmms.app.dto.location.response.LocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cmms.app.service.LocationService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping("/create")
    public ApiResponse<LocationResponse> createLocation(@RequestBody LocationCreateRequest request, @RequestParam Long tenantId) {
        return ApiResponse.<LocationResponse>builder()
                .result(locationService.createLocation(request, tenantId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<LocationResponse> getLocationById(@PathVariable Long id) {
        return ApiResponse.<LocationResponse>builder()
                .result(locationService.getLocationById(id))
                .build();
    }

    @GetMapping("/getAllByTenantId")
    public ApiResponse<BaseGetAllResponse<LocationResponse>> getAllLocationsByTenantId(@RequestParam Long tenantId) {
        return ApiResponse.<BaseGetAllResponse<LocationResponse>>builder()
                .result(locationService.getAllLocationsByTenantId(tenantId))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<LocationResponse> updateLocation(@PathVariable Long id, @RequestBody LocationUpdateRequest request) {
        request.setLocationId(id);
        return ApiResponse.<LocationResponse>builder()
                .result(locationService.updateLocation(request))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }
}
