package com.ruoyi.reimburse.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class ReimburseRequest  extends BaseEntity {
    SysReimburse reimburse;
    List<SysReimburseDetail> detailList;
    List<SysReimburseAttachment> attachmentList;

    public SysReimburse getReimburse() {
        return reimburse;
    }

    public void setReimburse(SysReimburse reimburse) {
        this.reimburse = reimburse;
    }

    public List<SysReimburseDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<SysReimburseDetail> detailList) {
        this.detailList = detailList;
    }

    public List<SysReimburseAttachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<SysReimburseAttachment> attachmentList) {
        this.attachmentList = attachmentList;
    }
}
