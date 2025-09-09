package com.cmms.app.mapper;

import com.cmms.app.dto.report.request.ReportCreateRequest;
import com.cmms.app.dto.report.request.ReportUpdateRequest;
import com.cmms.app.dto.report.response.ReportResponse;
import com.cmms.app.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    @Mapping(target = "reportId", ignore = true)
    @Mapping(target = "generatedAt", ignore = true)
    Report toEntity(ReportCreateRequest request);

    ReportResponse toResponse(Report entity);

    List<ReportResponse> toResponseList(List<Report> entities);

    @Mapping(target = "generatedAt", ignore = true)
    void updateReport(@MappingTarget Report entity, ReportUpdateRequest request);
}
