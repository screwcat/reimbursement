package com.ruoyi.reimburse.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

public class TravelStatistic {
    private String createBy;
    private String nickName;
    private List<TravelPeriod> travelPeriodList;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<TravelPeriod> getTravelPeriodList() {
        return travelPeriodList;
    }

    public void setTravelPeriodList(List<TravelPeriod> travelPeriodList) {
        this.travelPeriodList = travelPeriodList;
    }
}
