package com.cmms.app.service;

import com.cmms.app.dto.document.request.DocumentCreateRequest;
import com.cmms.app.dto.document.request.DocumentUpdateRequest;
import com.cmms.app.dto.document.response.DocumentResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.Document;
import com.cmms.app.mapper.DocumentMapper;
import com.cmms.app.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DocumentMapper documentMapper;

    public DocumentResponse createDocument(DocumentCreateRequest request) {
        Document entity = documentMapper.toEntity(request);
        entity.setUploadedAt(OffsetDateTime.now());
        Document saved = documentRepository.save(entity);
        return documentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DocumentResponse getDocumentById(Long id) {
        Document entity = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
        return documentMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<DocumentResponse> getAllDocuments() {
        List<Document> entities = documentRepository.findAll();
        List<DocumentResponse> responses = documentMapper.toResponseList(entities);
        return BaseGetAllResponse.<DocumentResponse>builder()
                .data(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<DocumentResponse> getDocumentsByAssetId(Long assetId) {
        List<Document> entities = documentRepository.findByAssetId(assetId);
        List<DocumentResponse> responses = documentMapper.toResponseList(entities);
        return BaseGetAllResponse.<DocumentResponse>builder()
                .data(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<DocumentResponse> getDocumentsByWorkOrderId(Long workOrderId) {
        List<Document> entities = documentRepository.findByWorkOrderId(workOrderId);
        List<DocumentResponse> responses = documentMapper.toResponseList(entities);
        return BaseGetAllResponse.<DocumentResponse>builder()
                .data(responses)
                .build();
    }

    public DocumentResponse updateDocument(Long id, DocumentUpdateRequest request) {
        Document entity = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
        documentMapper.updateDocument(entity, request);
        Document updated = documentRepository.save(entity);
        return documentMapper.toResponse(updated);
    }

    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new RuntimeException("Document not found with id: " + id);
        }
        documentRepository.deleteById(id);
    }
}
