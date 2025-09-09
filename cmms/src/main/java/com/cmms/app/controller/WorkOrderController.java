package com.cmms.app.controller;

import com.cmms.app.dto.workOrder.request.WorkOrderCreateRequest;
import com.cmms.app.dto.workOrder.request.WorkOrderGetAllRequest;
import com.cmms.app.dto.workOrder.request.WorkOrderUpdateRequest;
import com.cmms.app.dto.workOrder.response.WorkOrderResponse;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {
    @Autowired
    private WorkOrderService workOrderService;

    @PostMapping("/create")
    public ApiResponse<WorkOrderResponse> createWorkOrder(@RequestBody WorkOrderCreateRequest request) {
        return ApiResponse.<WorkOrderResponse>builder()
                .result(workOrderService.createWorkOrder(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<WorkOrderResponse> getWorkOrderById(@PathVariable Long id) {
        return ApiResponse.<WorkOrderResponse>builder()
                .result(workOrderService.getWorkOrderById(id))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<BaseGetAllResponse<WorkOrderResponse>> getAllWorkOrders(
            @RequestParam(defaultValue = "0") Integer skipCount,
            @RequestParam(defaultValue = "10") Integer maxResultCount,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return ApiResponse.<BaseGetAllResponse<WorkOrderResponse>>builder()
                .result(workOrderService.getAllWorkOrders(skipCount, maxResultCount, sortBy, sortDirection))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<WorkOrderResponse> updateWorkOrder(@PathVariable Long id, @RequestBody WorkOrderUpdateRequest request) {
        return ApiResponse.<WorkOrderResponse>builder()
                .result(workOrderService.updateWorkOrder(id, request))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteWorkOrder(@PathVariable Long id) {
        workOrderService.deleteWorkOrder(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/getAllByTenant")
    public ApiResponse<BaseGetAllResponse<WorkOrderResponse>> getAllByTenant(
            @RequestParam Long tenantId,
            @RequestParam(defaultValue = "0") Integer skipCount,
            @RequestParam(defaultValue = "10") Integer maxResultCount,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        WorkOrderGetAllRequest request = new WorkOrderGetAllRequest();
        request.setTenantId(tenantId);
        request.setSkipCount(skipCount);
        request.setMaxResultCount(maxResultCount);
        request.setSortBy(sortBy);
        request.setSortDirection(sortDirection);

        return ApiResponse.<BaseGetAllResponse<WorkOrderResponse>>builder()
                .result(workOrderService.getAllByTenant(request.getTenantId(), request.getSkipCount(), request.getMaxResultCount(), request.getSortBy(), request.getSortDirection()))
                .build();
    }
}
