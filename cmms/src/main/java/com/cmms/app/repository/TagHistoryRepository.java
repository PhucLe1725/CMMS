package com.cmms.app.repository;

import com.cmms.app.entity.TagHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TagHistoryRepository extends JpaRepository<TagHistory, Integer> {
    
    @Query(value = "SELECT * FROM tag_history ORDER BY t_stamp DESC LIMIT :limit", nativeQuery = true)
    List<TagHistory> findTopNOrderByTStampDesc(@Param("limit") int limit);
    
    @Query("SELECT th FROM TagHistory th WHERE th.tStamp BETWEEN :startTime AND :endTime ORDER BY th.tStamp DESC")
    List<TagHistory> findByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                   @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT th FROM TagHistory th ORDER BY th.tStamp DESC")
    List<TagHistory> findAllOrderByTStampDesc();
    
    @Query("SELECT COUNT(th) FROM TagHistory th")
    long countAll();
}
