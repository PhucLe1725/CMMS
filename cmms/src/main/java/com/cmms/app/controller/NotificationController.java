package com.cmms.app.controller;

import com.cmms.app.dto.notification.request.NotificationCreateRequest;
import com.cmms.app.dto.notification.request.NotificationUpdateRequest;
import com.cmms.app.dto.notification.response.NotificationResponse;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/create")
    public ApiResponse<NotificationResponse> createNotification(@RequestBody NotificationCreateRequest request) {
        return ApiResponse.<NotificationResponse>builder()
                .result(notificationService.createNotification(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<NotificationResponse> getNotificationById(@PathVariable Long id) {
        return ApiResponse.<NotificationResponse>builder()
                .result(notificationService.getNotificationById(id))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<BaseGetAllResponse<NotificationResponse>> getAllNotifications() {
        return ApiResponse.<BaseGetAllResponse<NotificationResponse>>builder()
                .result(notificationService.getAllNotifications())
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<NotificationResponse> updateNotification(@PathVariable Long id, @RequestBody NotificationUpdateRequest request) {
        return ApiResponse.<NotificationResponse>builder()
                .result(notificationService.updateNotification(id, request))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/getByTenantAndUser")
    public ApiResponse<BaseGetAllResponse<NotificationResponse>> getByTenantAndUser(
            @RequestParam Long tenantId, 
            @RequestParam Long userId) {
        return ApiResponse.<BaseGetAllResponse<NotificationResponse>>builder()
                .result(notificationService.getByTenantAndUser(tenantId, userId))
                .build();
    }

    @GetMapping("/getUnreadByTenantAndUser")
    public ApiResponse<BaseGetAllResponse<NotificationResponse>> getUnreadByTenantAndUser(
            @RequestParam Long tenantId, 
            @RequestParam Long userId) {
        return ApiResponse.<BaseGetAllResponse<NotificationResponse>>builder()
                .result(notificationService.getUnreadByTenantAndUser(tenantId, userId))
                .build();
    }

    @PutMapping("/markAsRead/{id}")
    public ApiResponse<NotificationResponse> markAsRead(@PathVariable Long id) {
        return ApiResponse.<NotificationResponse>builder()
                .result(notificationService.markAsRead(id))
                .build();
    }

    @GetMapping("/getByWorkOrder/{workOrderId}")
    public ApiResponse<BaseGetAllResponse<NotificationResponse>> getByWorkOrder(@PathVariable Long workOrderId) {
        return ApiResponse.<BaseGetAllResponse<NotificationResponse>>builder()
                .result(notificationService.getByWorkOrder(workOrderId))
                .build();
    }
}
