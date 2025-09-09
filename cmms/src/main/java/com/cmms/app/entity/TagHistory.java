package com.cmms.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tag_history")
public class TagHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_history_ndx")
    private Integer tagHistoryNdx;

    @Column(name = "tag_value_1")
    private Double tagValue1;

    @Column(name = "tag_value_2")
    private Double tagValue2;

    @Column(name = "string_value")
    private String stringValue;

    @Column(name = "t_stamp")
    private LocalDateTime tStamp;

    // Constructors
    public TagHistory() {}

    public TagHistory(Double tagValue1, Double tagValue2, String stringValue, LocalDateTime tStamp) {
        this.tagValue1 = tagValue1;
        this.tagValue2 = tagValue2;
        this.stringValue = stringValue;
        this.tStamp = tStamp;
    }

    // Getters and Setters
    public Integer getTagHistoryNdx() {
        return tagHistoryNdx;
    }

    public void setTagHistoryNdx(Integer tagHistoryNdx) {
        this.tagHistoryNdx = tagHistoryNdx;
    }

    public Double getTagValue1() {
        return tagValue1;
    }

    public void setTagValue1(Double tagValue1) {
        this.tagValue1 = tagValue1;
    }

    public Double getTagValue2() {
        return tagValue2;
    }

    public void setTagValue2(Double tagValue2) {
        this.tagValue2 = tagValue2;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public LocalDateTime getTStamp() {
        return tStamp;
    }

    public void setTStamp(LocalDateTime tStamp) {
        this.tStamp = tStamp;
    }

    @Override
    public String toString() {
        return "TagHistory{" +
                "tagHistoryNdx=" + tagHistoryNdx +
                ", tagValue1=" + tagValue1 +
                ", tagValue2=" + tagValue2 +
                ", stringValue='" + stringValue + '\'' +
                ", tStamp=" + tStamp +
                '}';
    }
}
