package com.cmms.app.repository;

import com.cmms.app.entity.IotSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface IotSensorRepository extends JpaRepository<IotSensor, Long> {
    
    List<IotSensor> findByAssetId(Long assetId);
    
    List<IotSensor> findBySensorType(String sensorType);
    
    List<IotSensor> findByAssetIdAndSensorType(Long assetId, String sensorType);
    
    @Query("SELECT i FROM IotSensor i WHERE i.recordedAt BETWEEN :startDate AND :endDate")
    List<IotSensor> findByRecordedAtBetween(@Param("startDate") OffsetDateTime startDate, 
                                           @Param("endDate") OffsetDateTime endDate);
    
    @Query("SELECT i FROM IotSensor i WHERE i.assetId = :assetId AND i.recordedAt BETWEEN :startDate AND :endDate")
    List<IotSensor> findByAssetIdAndRecordedAtBetween(@Param("assetId") Long assetId,
                                                     @Param("startDate") OffsetDateTime startDate,
                                                     @Param("endDate") OffsetDateTime endDate);
}
