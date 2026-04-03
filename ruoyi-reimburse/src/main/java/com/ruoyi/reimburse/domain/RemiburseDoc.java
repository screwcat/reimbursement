package com.ruoyi.reimburse.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报销单据主对象 remiburse_doc
 *
 * @author ruoyi
 * @date 2026-04-03
 */
public class RemiburseDoc extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 单据id */
    private Long docId;

    /** 单据编号 */
    private String billNo;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** 月度选择 */
    @Excel(name = "月度选择")
    private String monthSelect;

    /** 票据总数 */
    @Excel(name = "票据总数")
    private Long ticketTotal;

    /** 票据总金额 */
    @Excel(name = "票据总金额")
    private Long totalAmount;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date submitTime;

    /** 流程状态 */
    @Excel(name = "流程状态")
    private String processStatus;

    public void setDocId(Long docId)
    {
        this.docId = docId;
    }

    public Long getDocId()
    {
        return docId;
    }

    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }

    public String getBillNo()
    {
        return billNo;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
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

    public void setTotalAmount(Long totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public Long getTotalAmount()
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("docId", getDocId())
            .append("billNo", getBillNo())
            .append("userName", getUserName())
            .append("monthSelect", getMonthSelect())
            .append("ticketTotal", getTicketTotal())
            .append("totalAmount", getTotalAmount())
            .append("remark", getRemark())
            .append("submitTime", getSubmitTime())
            .append("processStatus", getProcessStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
