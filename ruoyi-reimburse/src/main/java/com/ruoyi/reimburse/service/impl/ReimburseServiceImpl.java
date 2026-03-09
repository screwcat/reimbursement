package com.ruoyi.reimburse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.reimburse.domain.Reimburse;
import com.ruoyi.reimburse.domain.ReimburseAttachment;
import com.ruoyi.reimburse.domain.ReimburseDetail;
import com.ruoyi.reimburse.mapper.ReimburseAttachmentMapper;
import com.ruoyi.reimburse.mapper.ReimburseDetailMapper;
import com.ruoyi.reimburse.mapper.ReimburseMapper;
import com.ruoyi.reimburse.service.IReimburseService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 报销申请Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class ReimburseServiceImpl extends ServiceImpl<ReimburseMapper, Reimburse> implements IReimburseService
{
    @Autowired
    private ReimburseMapper reimburseMapper;

    @Autowired
    private ReimburseDetailMapper reimburseDetailMapper;

    @Autowired
    private ReimburseAttachmentMapper reimburseAttachmentMapper;

    @Override
    public List<Reimburse> selectReimburseList(Reimburse reimburse)
    {
        return reimburseMapper.selectReimburseList(reimburse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertReimburse(Reimburse reimburse, List<ReimburseDetail> detailList, List<ReimburseAttachment> attachmentList)
    {
        // 生成单据编号
        reimburse.setBillNo(generateBillNo());
        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(detailList))
        {
            for (ReimburseDetail detail : detailList)
            {
                totalAmount = totalAmount.add(detail.getAmount());
            }
        }
        reimburse.setTotalAmount(totalAmount);
        // 设置默认状态为草稿
        reimburse.setProcessStatus("DRAFT");
        // 设置创建人
        reimburse.setCreateBy(SecurityUtils.getUsername());
        reimburse.setCreateTime(DateUtils.getNowDate());

        // 插入主表
        int rows = reimburseMapper.insert(reimburse);
        if (rows > 0 && StringUtils.isNotEmpty(detailList))
        {
            // 插入明细
            for (ReimburseDetail detail : detailList)
            {
                detail.setReimburseId(reimburse.getReimburseId());
                detail.setCreateTime(DateUtils.getNowDate());
                reimburseDetailMapper.insert(detail);
            }
        }
        if (rows > 0 && StringUtils.isNotEmpty(attachmentList))
        {
            // 插入附件
            for (ReimburseAttachment attachment : attachmentList)
            {
                attachment.setReimburseId(reimburse.getReimburseId());
                attachment.setCreateBy(SecurityUtils.getUsername());
                attachment.setCreateTime(DateUtils.getNowDate());
                reimburseAttachmentMapper.insert(attachment);
            }
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateReimburse(Reimburse reimburse, List<ReimburseDetail> detailList, List<ReimburseAttachment> attachmentList)
    {
        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(detailList))
        {
            for (ReimburseDetail detail : detailList)
            {
                totalAmount = totalAmount.add(detail.getAmount());
            }
        }
        reimburse.setTotalAmount(totalAmount);
        reimburse.setUpdateBy(SecurityUtils.getUsername());
        reimburse.setUpdateTime(DateUtils.getNowDate());

        // 更新主表
        int rows = reimburseMapper.updateById(reimburse);
        if (rows > 0)
        {
            // 删除原有明细
            reimburseDetailMapper.deleteReimburseDetailByReimburseId(reimburse.getReimburseId());
            // 插入新明细
            if (StringUtils.isNotEmpty(detailList))
            {
                for (ReimburseDetail detail : detailList)
                {
                    detail.setReimburseId(reimburse.getReimburseId());
                    detail.setUpdateTime(DateUtils.getNowDate());
                    reimburseDetailMapper.insert(detail);
                }
            }
            // 处理附件（这里简化处理：删除原有附件，插入新附件；实际可优化为增量更新）
            reimburseAttachmentMapper.deleteReimburseAttachmentByReimburseId(reimburse.getReimburseId());
            if (StringUtils.isNotEmpty(attachmentList))
            {
                for (ReimburseAttachment attachment : attachmentList)
                {
                    attachment.setReimburseId(reimburse.getReimburseId());
                    attachment.setCreateBy(SecurityUtils.getUsername());
                    attachment.setCreateTime(DateUtils.getNowDate());
                    reimburseAttachmentMapper.insert(attachment);
                }
            }
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteReimburseByIds(Long[] reimburseIds)
    {
        int rows = 0;
        for (Long reimburseId : reimburseIds)
        {
            // 删除明细
            reimburseDetailMapper.deleteReimburseDetailByReimburseId(reimburseId);
            // 删除附件
            reimburseAttachmentMapper.deleteReimburseAttachmentByReimburseId(reimburseId);
            // 删除主表
            rows += reimburseMapper.deleteById(reimburseId);
        }
        return rows;
    }

    @Override
    public int submitApproval(Long reimburseId)
    {
        Reimburse reimburse = reimburseMapper.selectById(reimburseId);
        if (reimburse == null)
        {
            return 0;
        }
        // 仅草稿状态可提交
        if (!"DRAFT".equals(reimburse.getProcessStatus()))
        {
            return 0;
        }
        reimburse.setProcessStatus("SUBMITTED");
        reimburse.setSubmitTime(DateUtils.getNowDate());
        reimburse.setUpdateBy(SecurityUtils.getUsername());
        reimburse.setUpdateTime(DateUtils.getNowDate());
        return reimburseMapper.updateById(reimburse);
    }

    @Override
    public int cancelApplication(Long reimburseId)
    {
        Reimburse reimburse = reimburseMapper.selectById(reimburseId);
        if (reimburse == null)
        {
            return 0;
        }
        // 已提交/审批中状态可撤销
        if (!"SUBMITTED".equals(reimburse.getProcessStatus()) && !"APPROVING".equals(reimburse.getProcessStatus()))
        {
            return 0;
        }
        reimburse.setProcessStatus("CANCELED");
        reimburse.setUpdateBy(SecurityUtils.getUsername());
        reimburse.setUpdateTime(DateUtils.getNowDate());
        return reimburseMapper.updateById(reimburse);
    }

    @Override
    public Map<String, Object> selectReimburseDetail(Long reimburseId)
    {
        Map<String, Object> result = new HashMap<>();
        // 查询主表
        Reimburse reimburse = reimburseMapper.selectById(reimburseId);
        result.put("reimburse", reimburse);
        // 查询明细
        List<ReimburseDetail> detailList = reimburseDetailMapper.selectReimburseDetailByReimburseId(reimburseId);
        result.put("detailList", detailList);
        // 查询附件
        List<ReimburseAttachment> attachmentList = reimburseAttachmentMapper.selectReimburseAttachmentByReimburseId(reimburseId);
        result.put("attachmentList", attachmentList);
        return result;
    }

    @Override
    public String generateBillNo()
    {
        // 规则：REIM + 年月日（yyyyMMdd） + 6位随机数
        String dateStr = DateUtils.dateTimeNow("yyyyMMdd");
        int randomNum = ThreadLocalRandom.current().nextInt(100000, 999999);
        String billNo = "REIM" + dateStr + randomNum;
        // 校验唯一性
//        while (reimburseMapper.selectReimburseByBillNo(billNo) != null)
//        {
            randomNum = ThreadLocalRandom.current().nextInt(100000, 999999);
            billNo = "REIM" + dateStr + randomNum;
//        }
        return billNo;
    }
}
