package com.ruoyi.reimburse.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.reimburse.domain.SysReimburseDetail;
import com.ruoyi.reimburse.mapper.SysReimburseDetailMapper;
import com.ruoyi.reimburse.service.ISysReimburseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 报销费用明细Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-11
 */
@Service
public class SysReimburseDetailServiceImpl extends ServiceImpl<SysReimburseDetailMapper, SysReimburseDetail> implements ISysReimburseDetailService
{
    @Autowired
    private SysReimburseDetailMapper sysReimburseDetailMapper;

    /**
     * 查询报销费用明细
     *
     * @param detailId 报销费用明细主键
     * @return 报销费用明细
     */
    @Override
    public SysReimburseDetail selectSysReimburseDetailByDetailId(Long detailId)
    {
        return sysReimburseDetailMapper.selectSysReimburseDetailByDetailId(detailId);
    }

    /**
     * 查询报销费用明细列表
     *
     * @param sysReimburseDetail 报销费用明细
     * @return 报销费用明细
     */
    @Override
    public List<SysReimburseDetail> selectSysReimburseDetailList(SysReimburseDetail sysReimburseDetail)
    {
        return sysReimburseDetailMapper.selectSysReimburseDetailList(sysReimburseDetail);
    }

    /**
     * 新增报销费用明细
     *
     * @param sysReimburseDetail 报销费用明细
     * @return 结果
     */
    @Override
    public int insertSysReimburseDetail(SysReimburseDetail sysReimburseDetail)
    {
        sysReimburseDetail.setCreateTime(DateUtils.getNowDate());
        return sysReimburseDetailMapper.insertSysReimburseDetail(sysReimburseDetail);
    }

    /**
     * 修改报销费用明细
     *
     * @param sysReimburseDetail 报销费用明细
     * @return 结果
     */
    @Override
    public int updateSysReimburseDetail(SysReimburseDetail sysReimburseDetail)
    {
        sysReimburseDetail.setUpdateTime(DateUtils.getNowDate());
        return sysReimburseDetailMapper.updateSysReimburseDetail(sysReimburseDetail);
    }

    /**
     * 批量删除报销费用明细
     *
     * @param detailIds 需要删除的报销费用明细主键
     * @return 结果
     */
    @Override
    public int deleteSysReimburseDetailByDetailIds(Long[] detailIds)
    {
        return sysReimburseDetailMapper.deleteSysReimburseDetailByDetailIds(detailIds);
    }

    @Override
    public int deleteSysReimburseDetailByReimburseId(Long reimburseId) {
        return sysReimburseDetailMapper.deleteSysReimburseDetailByReimburseId(reimburseId);
    }

    /**
     * 删除报销费用明细信息
     *
     * @param detailId 报销费用明细主键
     * @return 结果
     */
    @Override
    public int deleteSysReimburseDetailByDetailId(Long detailId)
    {
        return sysReimburseDetailMapper.deleteSysReimburseDetailByDetailId(detailId);
    }
}
