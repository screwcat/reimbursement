package com.ruoyi.reimburse.service;

import com.ruoyi.reimburse.domain.SysReimburseAttachment;

import java.util.List;

/**
 * 报销票据影像附件Service接口
 *
 * @author ruoyi
 * @date 2026-03-11
 */
public interface ISysReimburseAttachmentService
{
    /**
     * 查询报销票据影像附件
     *
     * @param attachmentId 报销票据影像附件主键
     * @return 报销票据影像附件
     */
    public SysReimburseAttachment selectSysReimburseAttachmentByAttachmentId(Long attachmentId);

    /**
     * 查询报销票据影像附件列表
     *
     * @param sysReimburseAttachment 报销票据影像附件
     * @return 报销票据影像附件集合
     */
    public List<SysReimburseAttachment> selectSysReimburseAttachmentList(SysReimburseAttachment sysReimburseAttachment);

    /**
     * 新增报销票据影像附件
     *
     * @param sysReimburseAttachment 报销票据影像附件
     * @return 结果
     */
    public int insertSysReimburseAttachment(SysReimburseAttachment sysReimburseAttachment);

    /**
     * 修改报销票据影像附件
     *
     * @param sysReimburseAttachment 报销票据影像附件
     * @return 结果
     */
    public int updateSysReimburseAttachment(SysReimburseAttachment sysReimburseAttachment);

    /**
     * 批量删除报销票据影像附件
     *
     * @param attachmentIds 需要删除的报销票据影像附件主键集合
     * @return 结果
     */
    public int deleteSysReimburseAttachmentByAttachmentIds(Long[] attachmentIds);

    /**
     * 删除报销票据影像附件信息
     *
     * @param attachmentId 报销票据影像附件主键
     * @return 结果
     */
    public int deleteSysReimburseAttachmentByAttachmentId(Long attachmentId);

    public int deleteSysReimburseAttachmentByReimburseId(Long reimburseId);
}
