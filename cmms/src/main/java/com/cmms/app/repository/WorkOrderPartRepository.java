package com.cmms.app.repository;

import com.cmms.app.entity.WorkOrderPart;
import com.cmms.app.entity.WorkOrderPart.WorkOrderPartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderPartRepository extends JpaRepository<WorkOrderPart, WorkOrderPartId> {
    List<WorkOrderPart> findByIdWorkOrderId(Long workOrderId);
    List<WorkOrderPart> findByIdPartId(Long partId);
}
