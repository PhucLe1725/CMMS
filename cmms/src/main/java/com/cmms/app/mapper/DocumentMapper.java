package com.cmms.app.mapper;

import com.cmms.app.dto.document.request.DocumentCreateRequest;
import com.cmms.app.dto.document.request.DocumentUpdateRequest;
import com.cmms.app.dto.document.response.DocumentResponse;
import com.cmms.app.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    
    @Mapping(target = "documentId", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    Document toEntity(DocumentCreateRequest request);

    DocumentResponse toResponse(Document entity);

    List<DocumentResponse> toResponseList(List<Document> entities);

    @Mapping(target = "uploadedAt", ignore = true)
    void updateDocument(@MappingTarget Document entity, DocumentUpdateRequest request);
}
