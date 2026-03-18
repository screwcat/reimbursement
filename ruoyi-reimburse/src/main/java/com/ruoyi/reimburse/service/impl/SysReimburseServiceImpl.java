package com.ruoyi.reimburse.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.reimburse.domain.ReimburseRequest;
import com.ruoyi.reimburse.domain.SysReimburse;
import com.ruoyi.reimburse.mapper.SysReimburseMapper;
import com.ruoyi.reimburse.service.ISysReimburseAttachmentService;
import com.ruoyi.reimburse.service.ISysReimburseDetailService;
import com.ruoyi.reimburse.service.ISysReimburseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 报销申请单主Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-07
 */
@Service
public class SysReimburseServiceImpl extends ServiceImpl<SysReimburseMapper,SysReimburse> implements ISysReimburseService
{
    @Autowired
    private SysReimburseMapper sysReimburseMapper;

    @Autowired
    private ISysReimburseDetailService sysReimburseDetailService;

    @Autowired
    private ISysReimburseAttachmentService sysReimburseAttachmentService;



    /**
     * 查询报销申请单主
     *
     * @param reimburseId 报销申请单主主键
     * @return 报销申请单主
     */
    @Override
    public SysReimburse selectSysReimburseByReimburseId(Long reimburseId)
    {
        return sysReimburseMapper.selectSysReimburseByReimburseId(reimburseId);
    }

    /**
     * 查询报销申请单主列表
     *
     * @param sysReimburse 报销申请单主
     * @return 报销申请单主
     */
    @Override
    public List<SysReimburse> selectSysReimburseList(SysReimburse sysReimburse)
    {
        return sysReimburseMapper.selectSysReimburseList(sysReimburse);
    }

    /**
     * 新增报销申请单主
     *
     * @param sysReimburse 报销申请单主
     * @return 结果
     */
    @Override
    public int insertSysReimburse(SysReimburse sysReimburse)
    {
        sysReimburse.setCreateTime(DateUtils.getNowDate());
        return sysReimburseMapper.insertSysReimburse(sysReimburse);
    }

    @Override
    public int createReimburse(ReimburseRequest reimburseRequest) {
        SysReimburse reimburse = reimburseRequest.getReimburse();
        int reimburseId = sysReimburseMapper.insertSysReimburse(reimburse);
        reimburseRequest.getDetailList().forEach(detail -> {
            detail.setReimburseId(Long.valueOf(reimburseId));
            sysReimburseDetailService.save(detail);
        });
        reimburseRequest.getAttachmentList().forEach(attachment -> {
            attachment.setReimburseId(Long.valueOf(reimburseId));
            sysReimburseAttachmentService.save(attachment);
        });
        return 0;
    }

    /**
     * 修改报销申请单主
     *
     * @param sysReimburse 报销申请单主
     * @return 结果
     */
    @Override
    public int updateSysReimburse(SysReimburse sysReimburse)
    {
        sysReimburse.setUpdateTime(DateUtils.getNowDate());
        return sysReimburseMapper.updateSysReimburse(sysReimburse);
    }

    /**
     * 批量删除报销申请单主
     *
     * @param reimburseIds 需要删除的报销申请单主主键
     * @return 结果
     */
    @Override
    public int deleteSysReimburseByReimburseIds(Long[] reimburseIds)
    {
        return sysReimburseMapper.deleteSysReimburseByReimburseIds(reimburseIds);
    }

    /**
     * 删除报销申请单主信息
     *
     * @param reimburseId 报销申请单主主键
     * @return 结果
     */
    @Override
    public int deleteSysReimburseByReimburseId(Long reimburseId)
    {
        return sysReimburseMapper.deleteSysReimburseByReimburseId(reimburseId);
    }
}
