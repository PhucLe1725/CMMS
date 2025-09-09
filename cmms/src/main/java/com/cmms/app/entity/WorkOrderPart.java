package com.cmms.app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "work_order_parts")
@Data
public class WorkOrderPart {
    @EmbeddedId
    private WorkOrderPartId id;

    @Column(name = "quantity_used", nullable = false)
    private Integer quantityUsed;

    @Embeddable
    @Data
    public static class WorkOrderPartId implements java.io.Serializable {
        @Column(name = "work_order_id")
        private Long workOrderId;

        @Column(name = "part_id")
        private Long partId;
    }
}