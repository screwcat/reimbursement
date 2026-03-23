package com.ruoyi.reimburse.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    /** 单据编号 */
    @Excel(name = "单据编号")
    private String billNo;

    @Excel(name = "提交人")
    private String nickName;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 月度选择(如2024-05) */
    @Excel(name = "月度")
    private String monthSelect;

    /** 票据总数 */
    @Excel(name = "票据总数")
    private Long ticketTotal;

    /** 票据总金额 */
    @Excel(name = "票据总金额")
    private BigDecimal totalAmount;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    /** 流程状态：DRAFT-草稿，SUBMITTED-已提交，APPROVING-审批中，APPROVED-已审批，REJECTED-已驳回，CANCELED-已撤销 */
    @Excel(name = "流程状态")
    private String processStatus;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setReimburseId(Long reimburseId)
    {
        this.reimburseId = reimburseId;
    }

    public Long getReimburseId()
    {
        return reimburseId;
    }

    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }

    public String getBillNo()
    {
        return billNo;
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

    public void setMonthSelect(String monthSelect)
    {
        this.monthSelect = monthSelect;
    }

    public String getMonthSelect()
    {
        return monthSelect;
    }

    public void setTicketTotal(Long ticketTotal)
    {
        this.ticketTotal = ticketTotal;
    }

    public Long getTicketTotal()
    {
        return ticketTotal;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
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


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("reimburseId", getReimburseId())
            .append("billNo", getBillNo())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("monthSelect", getMonthSelect())
            .append("ticketTotal", getTicketTotal())
            .append("totalAmount", getTotalAmount())
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
