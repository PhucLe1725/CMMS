package com.cmms.app.mapper;

import com.cmms.app.dto.location.request.LocationCreateRequest;
import com.cmms.app.dto.location.request.LocationUpdateRequest;
import com.cmms.app.dto.location.response.LocationResponse;
import com.cmms.app.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    @Mapping(target = "tenantId", ignore = true)
    Location toLocation(LocationCreateRequest locationCreateRequest);

    LocationResponse toLocationResponse(Location location);

    void updateLocation(@MappingTarget Location location, LocationUpdateRequest locationUpdateRequest);
}
