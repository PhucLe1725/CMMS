package com.cmms.app.service;

import com.cmms.app.dto.tagHistory.response.TagHistoryResponse;
import com.cmms.app.entity.TagHistory;
import com.cmms.app.mapper.TagHistoryMapper;
import com.cmms.app.repository.TagHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagHistoryRealtimeService {
    
    private final TagHistoryRepository tagHistoryRepository;
    private final TagHistoryMapper tagHistoryMapper;
    private final SimpMessagingTemplate messagingTemplate;
    
    /**
     * Save new tag data and broadcast to WebSocket subscribers
     */
    public TagHistoryResponse saveAndBroadcast(TagHistory tagHistory) {
        // Set current timestamp
        tagHistory.setTStamp(LocalDateTime.now());
        
        // Save to database
        TagHistory saved = tagHistoryRepository.save(tagHistory);
        TagHistoryResponse response = tagHistoryMapper.toResponse(saved);
        
        // Broadcast to all subscribers
        messagingTemplate.convertAndSend("/topic/tag-data", response);
        
        log.info("Broadcasting new tag data: ID={}", saved.getTagHistoryNdx());
        return response;
    }
    
    /**
     * Batch save and broadcast multiple tag data
     */
    public List<TagHistoryResponse> saveAndBroadcastBatch(List<TagHistory> tagHistoryList) {
        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        tagHistoryList.forEach(tag -> tag.setTStamp(now));
        
        // Save batch
        List<TagHistory> savedList = tagHistoryRepository.saveAll(tagHistoryList);
        List<TagHistoryResponse> responses = savedList.stream()
                .map(tagHistoryMapper::toResponse)
                .toList();
        
        // Broadcast batch data
        messagingTemplate.convertAndSend("/topic/tag-data-batch", responses);
        
        log.info("Broadcasting batch of {} tag data points", savedList.size());
        return responses;
    }
    
    /**
     * Get latest tag data
     */
    public List<TagHistoryResponse> getLatestData(int limit) {
        List<TagHistory> latestData = tagHistoryRepository.findTopNOrderByTStampDesc(limit);
        return latestData.stream()
                .map(tagHistoryMapper::toResponse)
                .toList();
    }
    
    /**
     * Get all tag data
     */
    public List<TagHistoryResponse> getAllData() {
        List<TagHistory> allData = tagHistoryRepository.findAllOrderByTStampDesc();
        return allData.stream()
                .map(tagHistoryMapper::toResponse)
                .toList();
    }
    
    /**
     * Send heartbeat to keep connection alive
     */
    public void sendHeartbeat() {
        messagingTemplate.convertAndSend("/topic/heartbeat", 
            java.util.Map.of("timestamp", LocalDateTime.now(), "status", "alive"));
    }
}
