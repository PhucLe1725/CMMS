package com.cmms.app.controller;

import com.cmms.app.dto.document.request.DocumentCreateRequest;
import com.cmms.app.dto.document.request.DocumentUpdateRequest;
import com.cmms.app.dto.document.response.DocumentResponse;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @PostMapping("/create")
    public ApiResponse<DocumentResponse> createDocument(@RequestBody DocumentCreateRequest request) {
        return ApiResponse.<DocumentResponse>builder()
                .result(documentService.createDocument(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DocumentResponse> getDocumentById(@PathVariable Long id) {
        return ApiResponse.<DocumentResponse>builder()
                .result(documentService.getDocumentById(id))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<BaseGetAllResponse<DocumentResponse>> getAllDocuments() {
        return ApiResponse.<BaseGetAllResponse<DocumentResponse>>builder()
                .result(documentService.getAllDocuments())
                .build();
    }

    @GetMapping("/getByAsset")
    public ApiResponse<BaseGetAllResponse<DocumentResponse>> getDocumentsByAssetId(@RequestParam Long assetId) {
        return ApiResponse.<BaseGetAllResponse<DocumentResponse>>builder()
                .result(documentService.getDocumentsByAssetId(assetId))
                .build();
    }

    @GetMapping("/getByWorkOrder")
    public ApiResponse<BaseGetAllResponse<DocumentResponse>> getDocumentsByWorkOrderId(@RequestParam Long workOrderId) {
        return ApiResponse.<BaseGetAllResponse<DocumentResponse>>builder()
                .result(documentService.getDocumentsByWorkOrderId(workOrderId))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<DocumentResponse> updateDocument(@PathVariable Long id, @RequestBody DocumentUpdateRequest request) {
        return ApiResponse.<DocumentResponse>builder()
                .result(documentService.updateDocument(id, request))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ApiResponse.<Void>builder().build();
    }
}
