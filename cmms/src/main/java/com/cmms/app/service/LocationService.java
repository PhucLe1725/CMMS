package com.cmms.app.service;

import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.dto.location.request.LocationCreateRequest;
import com.cmms.app.dto.location.request.LocationUpdateRequest;
import com.cmms.app.dto.location.response.LocationResponse;
import com.cmms.app.entity.Location;
import com.cmms.app.mapper.LocationMapper;
import com.cmms.app.repository.LocationRepository;
import com.cmms.app.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TenantRepository tenantRepository;

    public LocationResponse createLocation(LocationCreateRequest request, Long tenantId) {
        Location location = locationMapper.toLocation(request);
        location.setTenantId(tenantId);

        location = locationRepository.save(location);
        return locationMapper.toLocationResponse(location);
    }

    public LocationResponse getLocationById(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        return locationMapper.toLocationResponse(location);
    }

    public BaseGetAllResponse<LocationResponse> getAllLocationsByTenantId(Long tenantId) {
        List<LocationResponse> locationResponses = locationRepository.findAllLocationByTenantId(tenantId)
                .stream()
                .map(locationMapper::toLocationResponse)
                .toList();

        return BaseGetAllResponse.<LocationResponse>builder()
                .data(locationResponses)
                .build();
    }

    public LocationResponse updateLocation(LocationUpdateRequest request) {
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        locationMapper.updateLocation(location, request);
        location = locationRepository.save(location);
        return locationMapper.toLocationResponse(location);
    }

    public void deleteLocation(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        locationRepository.delete(location);
    }
}
