package com.ruoyi.reimburse.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.poi.ExcelDictHandler;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报销申请单主对象 sys_reimburse
 *
 * @author ruoyi
 * @date 2026-03-07
 */
public class SysReimburse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报销单ID */
    private Long reimburseId;

    private Long docId;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    private String nickName;

    private BigDecimal totalAmount;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    /** 流程状态：DRAFT-草稿，SUBMITTED-已提交，APPROVING-审批中，APPROVED-已审批，REJECTED-已驳回，CANCELED-已撤销 */
    @Excel(name = "流程状态", handler = ExcelDictHandler.class, args = {"process_status"})
    private String processStatus;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private Long ticketTotal;

    private int daysBetween;

    public void setReimburseId(Long reimburseId)
    {
        this.reimburseId = reimburseId;
    }

    public Long getReimburseId()
    {
        return reimburseId;
    }


    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setSubmitTime(Date submitTime)
    {
        this.submitTime = submitTime;
    }

    public Date getSubmitTime()
    {
        return submitTime;
    }

    public void setProcessStatus(String processStatus)
    {
        this.processStatus = processStatus;
    }

    public String getProcessStatus()
    {
        return processStatus;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTicketTotal() {
        return ticketTotal;
    }

    public void setTicketTotal(Long ticketTotal) {
        this.ticketTotal = ticketTotal;
    }

    public int getDaysBetween() {
        return daysBetween;
    }

    public void setDaysBetween(int daysBetween) {
        this.daysBetween = daysBetween;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("reimburseId", getReimburseId())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("remark", getRemark())
            .append("submitTime", getSubmitTime())
            .append("processStatus", getProcessStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
