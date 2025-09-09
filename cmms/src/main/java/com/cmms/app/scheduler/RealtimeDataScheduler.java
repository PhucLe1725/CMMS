package com.cmms.app.scheduler;

import com.cmms.app.entity.TagHistory;
import com.cmms.app.service.TagHistoryRealtimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RealtimeDataScheduler {
    
    private final TagHistoryRealtimeService tagHistoryService;
    
    /**
     * Send heartbeat every 30 seconds to keep WebSocket connections alive
     */
    @Scheduled(fixedRate = 30000) // 30 seconds
    public void sendHeartbeat() {
        try {
            tagHistoryService.sendHeartbeat();
        } catch (Exception e) {
            log.error("Error sending heartbeat: ", e);
        }
    }
    
    /**
     * Optional: Simulate tag data for demo purposes
     * Remove this in production
     */
    // @Scheduled(fixedRate = 5000) // Every 5 seconds
    // public void simulateTagData() {
    //     try {
    //         TagHistory demoData = new TagHistory();
    //         demoData.setTagValue1(20.0 + Math.random() * 10); // Random temp 20-30°C
    //         demoData.setTagValue2(65.0 + Math.random() * 20); // Random humidity 65-85%
    //         demoData.setStringValue("Normal");
    //         
    //         tagHistoryService.saveAndBroadcast(demoData);
    //         log.debug("Simulated tag data sent");
    //     } catch (Exception e) {
    //         log.error("Error simulating tag data: ", e);
    //     }
    // }
}
