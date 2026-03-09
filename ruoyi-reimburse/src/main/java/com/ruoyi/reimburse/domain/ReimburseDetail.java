package com.ruoyi.reimburse.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 报销费用明细表
 *
 * @author ruoyi
 */
@Data
@TableName("sys_reimburse_detail")
public class ReimburseDetail
{
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    @TableId(type = IdType.AUTO)
    private Long detailId;

    /** 报销单ID */
    private Long reimburseId;

    /** 票据类型 */
    private String ticketType;

    /** 票据张数 */
    private Integer ticketNum;

    /** 起始地 */
    private String startPlace;

    /** 目的地 */
    private String endPlace;

    /** 票据日期 */
    private Date ticketDate;

    /** 款项金额 */
    private BigDecimal amount;

    /** 住宿地点 */
    private String accommodation;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}
