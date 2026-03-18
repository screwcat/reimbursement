package com.ruoyi.reimburse.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.reimburse.domain.SysReimburseAttachment;
import com.ruoyi.reimburse.mapper.SysReimburseAttachmentMapper;
import com.ruoyi.reimburse.service.ISysReimburseAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 报销票据影像附件Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-11
 */
@Service
public class SysReimburseAttachmentServiceImpl extends ServiceImpl<SysReimburseAttachmentMapper, SysReimburseAttachment> implements ISysReimburseAttachmentService
{
    @Autowired
    private SysReimburseAttachmentMapper sysReimburseAttachmentMapper;

    /**
     * 查询报销票据影像附件
     *
     * @param attachmentId 报销票据影像附件主键
     * @return 报销票据影像附件
     */
    @Override
    public SysReimburseAttachment selectSysReimburseAttachmentByAttachmentId(Long attachmentId)
    {
        return sysReimburseAttachmentMapper.selectSysReimburseAttachmentByAttachmentId(attachmentId);
    }

    /**
     * 查询报销票据影像附件列表
     *
     * @param sysReimburseAttachment 报销票据影像附件
     * @return 报销票据影像附件
     */
    @Override
    public List<SysReimburseAttachment> selectSysReimburseAttachmentList(SysReimburseAttachment sysReimburseAttachment)
    {
        return sysReimburseAttachmentMapper.selectSysReimburseAttachmentList(sysReimburseAttachment);
    }

    /**
     * 新增报销票据影像附件
     *
     * @param sysReimburseAttachment 报销票据影像附件
     * @return 结果
     */
    @Override
    public int insertSysReimburseAttachment(SysReimburseAttachment sysReimburseAttachment)
    {
        sysReimburseAttachment.setCreateTime(DateUtils.getNowDate());
        return sysReimburseAttachmentMapper.insertSysReimburseAttachment(sysReimburseAttachment);
    }

    /**
     * 修改报销票据影像附件
     *
     * @param sysReimburseAttachment 报销票据影像附件
     * @return 结果
     */
    @Override
    public int updateSysReimburseAttachment(SysReimburseAttachment sysReimburseAttachment)
    {
        return sysReimburseAttachmentMapper.updateSysReimburseAttachment(sysReimburseAttachment);
    }

    /**
     * 批量删除报销票据影像附件
     *
     * @param attachmentIds 需要删除的报销票据影像附件主键
     * @return 结果
     */
    @Override
    public int deleteSysReimburseAttachmentByAttachmentIds(Long[] attachmentIds)
    {
        return sysReimburseAttachmentMapper.deleteSysReimburseAttachmentByAttachmentIds(attachmentIds);
    }

    /**
     * 删除报销票据影像附件信息
     *
     * @param attachmentId 报销票据影像附件主键
     * @return 结果
     */
    @Override
    public int deleteSysReimburseAttachmentByAttachmentId(Long attachmentId)
    {
        return sysReimburseAttachmentMapper.deleteSysReimburseAttachmentByAttachmentId(attachmentId);
    }
}
