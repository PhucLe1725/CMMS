package com.cmms.app.controller;

import com.cmms.app.dto.tagHistory.response.TagHistoryResponse;
import com.cmms.app.entity.TagHistory;
import com.cmms.app.service.TagHistoryRealtimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TagHistoryWebSocketController {
    
    private final TagHistoryRealtimeService tagHistoryService;
    
    /**
     * Handle subscription to real-time tag data
     */
    @SubscribeMapping("/topic/tag-data")
    public String subscribeToTagData() {
        log.info("Client subscribed to tag data");
        return "Subscribed to real-time tag data";
    }
    
    /**
     * REST endpoint to manually send tag data (for testing or external systems)
     */
    @PostMapping("/api/tag-history/send-data")
    @ResponseBody
    public TagHistoryResponse sendTagData(@RequestBody TagHistory tagHistory) {
        return tagHistoryService.saveAndBroadcast(tagHistory);
    }
    
    /**
     * REST endpoint to send batch tag data
     */
    @PostMapping("/api/tag-history/send-batch")
    @ResponseBody
    public List<TagHistoryResponse> sendTagDataBatch(@RequestBody List<TagHistory> tagHistoryList) {
        return tagHistoryService.saveAndBroadcastBatch(tagHistoryList);
    }
    
    /**
     * Get latest data
     */
    @GetMapping("/api/tag-history/latest")
    @ResponseBody
    public List<TagHistoryResponse> getLatestData(@RequestParam(defaultValue = "10") int limit) {
        return tagHistoryService.getLatestData(limit);
    }
    
    /**
     * Get all data
     */
    @GetMapping("/api/tag-history/all")
    @ResponseBody
    public List<TagHistoryResponse> getAllData() {
        return tagHistoryService.getAllData();
    }
    
    /**
     * WebSocket message handler for receiving tag data from IoT devices
     */
    @MessageMapping("/send-tag-data")
    public void handleTagData(@Payload Map<String, Object> payload) {
        try {
            TagHistory tagHistory = new TagHistory();
            tagHistory.setTagValue1(payload.get("tagValue1") != null ? 
                Double.valueOf(payload.get("tagValue1").toString()) : null);
            tagHistory.setTagValue2(payload.get("tagValue2") != null ? 
                Double.valueOf(payload.get("tagValue2").toString()) : null);
            tagHistory.setStringValue(payload.get("stringValue") != null ? 
                payload.get("stringValue").toString() : null);
            
            tagHistoryService.saveAndBroadcast(tagHistory);
            
        } catch (Exception e) {
            log.error("Error processing tag data: ", e);
        }
    }
}
