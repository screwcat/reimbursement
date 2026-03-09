package com.ruoyi.reimburse.mapper;

import com.ruoyi.reimburse.domain.SysReimburse;

import java.util.List;

/**
 * 报销申请单主Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-07
 */
public interface SysReimburseMapper
{
    /**
     * 查询报销申请单主
     *
     * @param reimburseId 报销申请单主主键
     * @return 报销申请单主
     */
    public SysReimburse selectSysReimburseByReimburseId(Long reimburseId);

    /**
     * 查询报销申请单主列表
     *
     * @param sysReimburse 报销申请单主
     * @return 报销申请单主集合
     */
    public List<SysReimburse> selectSysReimburseList(SysReimburse sysReimburse);

    /**
     * 新增报销申请单主
     *
     * @param sysReimburse 报销申请单主
     * @return 结果
     */
    public int insertSysReimburse(SysReimburse sysReimburse);

    /**
     * 修改报销申请单主
     *
     * @param sysReimburse 报销申请单主
     * @return 结果
     */
    public int updateSysReimburse(SysReimburse sysReimburse);

    /**
     * 删除报销申请单主
     *
     * @param reimburseId 报销申请单主主键
     * @return 结果
     */
    public int deleteSysReimburseByReimburseId(Long reimburseId);

    /**
     * 批量删除报销申请单主
     *
     * @param reimburseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysReimburseByReimburseIds(Long[] reimburseIds);
}
