package com.cmms.app.repository;

import com.cmms.app.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByAssetId(Long assetId);
    List<Document> findByWorkOrderId(Long workOrderId);
    List<Document> findByFileName(String fileName);
    List<Document> findByAssetIdOrWorkOrderId(Long assetId, Long workOrderId);
}
