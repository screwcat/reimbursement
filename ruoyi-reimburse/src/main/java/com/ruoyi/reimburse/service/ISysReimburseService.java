package com.ruoyi.reimburse.service;

import java.util.List;

import com.ruoyi.reimburse.domain.ReimburseRequest;
import com.ruoyi.reimburse.domain.SysReimburse;
import com.ruoyi.reimburse.domain.TravelStatistic;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 报销申请单主Service接口
 *
 * @author ruoyi
 * @date 2026-03-07
 */
public interface ISysReimburseService
{
    /**
     * 查询报销申请单主
     *
     * @param reimburseId 报销申请单主主键
     * @return 报销申请单主
     */
    public SysReimburse selectSysReimburseByReimburseId(Long reimburseId);

    public ReimburseRequest getReimburseInfo(Long reimburseId);

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
    public int updateSysReimburse(ReimburseRequest reimburseRequest);

    /**
     * 批量删除报销申请单主
     *
     * @param reimburseIds 需要删除的报销申请单主主键集合
     * @return 结果
     */
    public int deleteSysReimburseByReimburseIds(Long[] reimburseIds);

    /**
     * 删除报销申请单主信息
     *
     * @param reimburseId 报销申请单主主键
     * @return 结果
     */
    public int deleteSysReimburseByReimburseId(Long reimburseId);

    public int createReimburse(ReimburseRequest reimburseRequest);

    public int changeProcessState(Long reimburseId, String processState);

    public int submitReimburse(Long reimburseId);

    public List<TravelStatistic> getTravelStatistics(String startMonth, String endMonth);

    public boolean checkTimePeriod(String startDate, String endDate, String userName);
}
