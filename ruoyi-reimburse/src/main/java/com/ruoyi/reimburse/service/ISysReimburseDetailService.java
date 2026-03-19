package com.ruoyi.reimburse.service;

import com.ruoyi.reimburse.domain.SysReimburseDetail;

import java.util.List;

/**
 * 报销费用明细Service接口
 *
 * @author ruoyi
 * @date 2026-03-11
 */
public interface ISysReimburseDetailService
{
    /**
     * 查询报销费用明细
     *
     * @param detailId 报销费用明细主键
     * @return 报销费用明细
     */
    public SysReimburseDetail selectSysReimburseDetailByDetailId(Long detailId);

    /**
     * 查询报销费用明细列表
     *
     * @param sysReimburseDetail 报销费用明细
     * @return 报销费用明细集合
     */
    public List<SysReimburseDetail> selectSysReimburseDetailList(SysReimburseDetail sysReimburseDetail);

    /**
     * 新增报销费用明细
     *
     * @param sysReimburseDetail 报销费用明细
     * @return 结果
     */
    public int insertSysReimburseDetail(SysReimburseDetail sysReimburseDetail);

    /**
     * 修改报销费用明细
     *
     * @param sysReimburseDetail 报销费用明细
     * @return 结果
     */
    public int updateSysReimburseDetail(SysReimburseDetail sysReimburseDetail);

    /**
     * 批量删除报销费用明细
     *
     * @param detailIds 需要删除的报销费用明细主键集合
     * @return 结果
     */
    public int deleteSysReimburseDetailByDetailIds(Long[] detailIds);

    public int deleteSysReimburseDetailByReimburseId(Long reimburseId);

    /**
     * 删除报销费用明细信息
     *
     * @param detailId 报销费用明细主键
     * @return 结果
     */
    public int deleteSysReimburseDetailByDetailId(Long detailId);
}
