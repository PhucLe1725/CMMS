package com.cmms.app.mapper;

import com.cmms.app.dto.tagHistory.response.TagHistoryResponse;
import com.cmms.app.entity.TagHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagHistoryMapper {
    @Mapping(source = "TStamp", target = "tStamp")
    TagHistoryResponse toResponse(TagHistory tagHistory);
}
