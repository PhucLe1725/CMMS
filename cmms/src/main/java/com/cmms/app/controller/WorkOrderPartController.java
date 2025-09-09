package com.cmms.app.controller;

import com.cmms.app.dto.workOrderPart.request.WorkOrderPartCreateRequest;
import com.cmms.app.dto.workOrderPart.request.WorkOrderPartUpdateRequest;
import com.cmms.app.dto.workOrderPart.response.WorkOrderPartResponse;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.service.WorkOrderPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-order-parts")
public class WorkOrderPartController {
    @Autowired
    private WorkOrderPartService workOrderPartService;

    @PostMapping("/create")
    public ApiResponse<WorkOrderPartResponse> createWorkOrderPart(@RequestBody WorkOrderPartCreateRequest request) {
        return ApiResponse.<WorkOrderPartResponse>builder()
                .result(workOrderPartService.createWorkOrderPart(request))
                .build();
    }

    @GetMapping("/{workOrderId}/{partId}")
    public ApiResponse<WorkOrderPartResponse> getWorkOrderPartById(@PathVariable Long workOrderId, @PathVariable Long partId) {
        return ApiResponse.<WorkOrderPartResponse>builder()
                .result(workOrderPartService.getWorkOrderPartById(workOrderId, partId))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<BaseGetAllResponse<WorkOrderPartResponse>> getAllWorkOrderParts() {
        return ApiResponse.<BaseGetAllResponse<WorkOrderPartResponse>>builder()
                .result(workOrderPartService.getAllWorkOrderParts())
                .build();
    }

    @GetMapping("/getByWorkOrder")
    public ApiResponse<BaseGetAllResponse<WorkOrderPartResponse>> getWorkOrderPartsByWorkOrderId(@RequestParam Long workOrderId) {
        return ApiResponse.<BaseGetAllResponse<WorkOrderPartResponse>>builder()
                .result(workOrderPartService.getWorkOrderPartsByWorkOrderId(workOrderId))
                .build();
    }

    @GetMapping("/getByPart")
    public ApiResponse<BaseGetAllResponse<WorkOrderPartResponse>> getWorkOrderPartsByPartId(@RequestParam Long partId) {
        return ApiResponse.<BaseGetAllResponse<WorkOrderPartResponse>>builder()
                .result(workOrderPartService.getWorkOrderPartsByPartId(partId))
                .build();
    }

    @PutMapping("/update/{workOrderId}/{partId}")
    public ApiResponse<WorkOrderPartResponse> updateWorkOrderPart(@PathVariable Long workOrderId, @PathVariable Long partId, @RequestBody WorkOrderPartUpdateRequest request) {
        return ApiResponse.<WorkOrderPartResponse>builder()
                .result(workOrderPartService.updateWorkOrderPart(workOrderId, partId, request))
                .build();
    }

    @DeleteMapping("/delete/{workOrderId}/{partId}")
    public ApiResponse<Void> deleteWorkOrderPart(@PathVariable Long workOrderId, @PathVariable Long partId) {
        workOrderPartService.deleteWorkOrderPart(workOrderId, partId);
        return ApiResponse.<Void>builder().build();
    }
}
