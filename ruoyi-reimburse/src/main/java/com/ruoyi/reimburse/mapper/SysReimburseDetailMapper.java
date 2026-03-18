package com.ruoyi.reimburse.mapper;

import com.ruoyi.reimburse.domain.SysReimburseDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 报销费用明细Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-11
 */
@Mapper
public interface SysReimburseDetailMapper
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
     * 删除报销费用明细
     *
     * @param detailId 报销费用明细主键
     * @return 结果
     */
    public int deleteSysReimburseDetailByDetailId(Long detailId);

    /**
     * 批量删除报销费用明细
     *
     * @param detailIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysReimburseDetailByDetailIds(Long[] detailIds);
}
