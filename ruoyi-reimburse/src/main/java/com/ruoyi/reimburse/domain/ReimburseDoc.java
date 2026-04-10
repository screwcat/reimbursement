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
 * 报销单据主对象 reimburse_doc
 *
 * @author ruoyi
 * @date 2026-04-03
 */
public class ReimburseDoc extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 单据id */
    private Long docId;

    /** 单据编号 */
    @Excel(name = "单据编号",sort = 0)
    private String billNo;

    /** 用户名 */
    private String userName;

    /** 月度选择 */
    @Excel(name = "报销月度",sort = 2)
    private String monthSelect;

    /** 票据总金额 */
    private Long totalAmount;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss",sort = 6)
    private Date submitTime;

    /** 流程状态 */
    @Excel(name = "流程状态",sort = 7,handler = ExcelDictHandler.class, args = {"process_status"})
    private String processStatus;

    /** 提交人 */
    @Excel(name = "提交人",sort = 1)
    private String nickName;

    @Excel(name = "票据总数",sort = 3)
    private int billsNumber;

    @Excel(name = "票据总金额",sort = 5)
    private BigDecimal amount;

    @Excel(name = "总天数",sort = 4)
    private int daysBetween;

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

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public int getBillsNumber() {
        return billsNumber;
    }

    public void setBillsNumber(int billsNumber) {
        this.billsNumber = billsNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
            .append("docId", getDocId())
            .append("billNo", getBillNo())
            .append("userName", getUserName())
            .append("monthSelect", getMonthSelect())
            .append("totalAmount", getTotalAmount())
            .append("remark", getRemark())
            .append("submitTime", getSubmitTime())
            .append("processStatus", getProcessStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
