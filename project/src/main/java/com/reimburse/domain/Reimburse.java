package com.reimburse.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 报销申请表单主表
 *
 * @author ruoyi
 */
@Data
@TableName("sys_reimburse")
public class Reimburse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报销单ID */
    @TableId(type = IdType.AUTO)
    private Long reimburseId;

    /** 单据编号 */
    private String billNo;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 月度选择 */
    private String monthSelect;

    /** 票据总数 */
    private Integer ticketTotal;

    /** 票据总金额 */
    private BigDecimal totalAmount;

    /** 备注 */
    private String remark;

    /** 提交时间 */
    private Date submitTime;

    /** 流程状态：DRAFT-草稿，SUBMITTED-已提交，APPROVING-审批中，APPROVED-已审批，REJECTED-已驳回，CANCELED-已撤销 */
    private String processStatus;
}
