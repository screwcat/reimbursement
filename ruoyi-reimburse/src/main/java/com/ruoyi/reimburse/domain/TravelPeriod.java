package com.ruoyi.reimburse.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.List;

public class TravelPeriod {
    private String startTime;
    private String endTime;
    private BigDecimal totalAmount;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
