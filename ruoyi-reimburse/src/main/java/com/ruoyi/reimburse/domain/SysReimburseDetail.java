package com.ruoyi.reimburse.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报销费用明细对象 sys_reimburse_detail
 *
 * @author ruoyi
 * @date 2026-03-11
 */
@Data
@TableName("sys_reimburse_detail")
public class SysReimburseDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long detailId;

    /** 报销单ID */
    @Excel(name = "报销单ID")
    private Long reimburseId;

    /** 票据类型(如机票、火车票、住宿费) */
    @Excel(name = "票据类型(如机票、火车票、住宿费)")
    private String ticketType;

    /** 票据张数 */
    @Excel(name = "票据张数")
    private Long ticketNum;

    /** 起始地 */
    @Excel(name = "起始地")
    private String startPlace;

    /** 目的地 */
    @Excel(name = "目的地")
    private String endPlace;

    /** 票据日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "票据日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ticketDate;

    /** 款项金额 */
    @Excel(name = "款项金额")
    private BigDecimal amount;

    /** 住宿地点 */
    @Excel(name = "住宿地点")
    private String accommodation;

    public void setDetailId(Long detailId)
    {
        this.detailId = detailId;
    }

    public Long getDetailId()
    {
        return detailId;
    }

    public void setReimburseId(Long reimburseId)
    {
        this.reimburseId = reimburseId;
    }

    public Long getReimburseId()
    {
        return reimburseId;
    }

    public void setTicketType(String ticketType)
    {
        this.ticketType = ticketType;
    }

    public String getTicketType()
    {
        return ticketType;
    }

    public void setTicketNum(Long ticketNum)
    {
        this.ticketNum = ticketNum;
    }

    public Long getTicketNum()
    {
        return ticketNum;
    }

    public void setStartPlace(String startPlace)
    {
        this.startPlace = startPlace;
    }

    public String getStartPlace()
    {
        return startPlace;
    }

    public void setEndPlace(String endPlace)
    {
        this.endPlace = endPlace;
    }

    public String getEndPlace()
    {
        return endPlace;
    }

    public void setTicketDate(Date ticketDate)
    {
        this.ticketDate = ticketDate;
    }

    public Date getTicketDate()
    {
        return ticketDate;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAccommodation(String accommodation)
    {
        this.accommodation = accommodation;
    }

    public String getAccommodation()
    {
        return accommodation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("detailId", getDetailId())
            .append("reimburseId", getReimburseId())
            .append("ticketType", getTicketType())
            .append("ticketNum", getTicketNum())
            .append("startPlace", getStartPlace())
            .append("endPlace", getEndPlace())
            .append("ticketDate", getTicketDate())
            .append("amount", getAmount())
            .append("accommodation", getAccommodation())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
