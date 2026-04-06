package com.ruoyi.reimburse.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.reimburse.domain.*;
import com.ruoyi.reimburse.mapper.SysReimburseMapper;
import com.ruoyi.reimburse.service.ISysReimburseAttachmentService;
import com.ruoyi.reimburse.service.ISysReimburseDetailService;
import com.ruoyi.reimburse.service.ISysReimburseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 报销申请单主Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-07
 */
@Service
public class SysReimburseServiceImpl implements ISysReimburseService
{
    @Autowired
    private SysReimburseMapper sysReimburseMapper;

    @Autowired
    private ISysReimburseDetailService sysReimburseDetailService;

    @Autowired
    private ISysReimburseAttachmentService sysReimburseAttachmentService;

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
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

    @Override
    public ReimburseRequest getReimburseInfo(Long reimburseId) {
        ReimburseRequest reimburseRequest = new ReimburseRequest();
        SysReimburseDetail sysReimburseDetail = new SysReimburseDetail();
        SysReimburseAttachment sysReimburseAttachment = new SysReimburseAttachment();
        sysReimburseDetail.setReimburseId(reimburseId);
        sysReimburseAttachment.setReimburseId(reimburseId);
        SysReimburse sysReimburse = sysReimburseMapper.selectSysReimburseByReimburseId(reimburseId);
        List<SysReimburseDetail> detailList = sysReimburseDetailService.selectSysReimburseDetailList(sysReimburseDetail);
        List<SysReimburseAttachment> attachList = sysReimburseAttachmentService.selectSysReimburseAttachmentList(sysReimburseAttachment);
        reimburseRequest.setReimburse(sysReimburse);
        reimburseRequest.setDetailList(detailList);
        reimburseRequest.setAttachmentList(attachList);
        return reimburseRequest;
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
        if(!isAdmin()&&!hasRole("finance")){
            sysReimburse.setCreateBy(getUsername());
        }
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

    /**
     * 修改报销申请单主
     *
     * @param reimburseRequest 报销申请单主
     * @return 结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateSysReimburse(ReimburseRequest reimburseRequest)
    {
        SysReimburse sysReimburse = reimburseRequest.getReimburse();
        sysReimburse.setUpdateTime(DateUtils.getNowDate());
        sysReimburseDetailService.deleteSysReimburseDetailByReimburseId(sysReimburse.getReimburseId());
        sysReimburseAttachmentService.deleteSysReimburseAttachmentByReimburseId(sysReimburse.getReimburseId());
        createDtl(reimburseRequest,sysReimburse.getReimburseId());
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int createReimburse(ReimburseRequest reimburseRequest) {
        SysReimburse reimburse = reimburseRequest.getReimburse();
        reimburse.setProcessStatus("DRAFT");
        reimburse.setCreateBy(getUsername());
        int res = sysReimburseMapper.insertSysReimburse(reimburse);
        createDtl(reimburseRequest,reimburse.getReimburseId());
        return res;
    }

    public void createDtl(ReimburseRequest reimburseRequest,Long reimburseId) {
        SysReimburse reimburse = reimburseRequest.getReimburse();
        reimburseRequest.getDetailList().forEach(detail -> {
            detail.setReimburseId(reimburseId);
            sysReimburseDetailService.insertSysReimburseDetail(detail);
        });
        reimburseRequest.getAttachmentList().forEach(attachment -> {
            attachment.setReimburseId(reimburseId);
            sysReimburseAttachmentService.insertSysReimburseAttachment(attachment);
        });
     }

    @Override
    public int changeProcessState(Long reimburseId, String processState) {
        return sysReimburseMapper.changeProcessState(reimburseId, processState);
    }

    @Override
    public int submitReimburse(Long reimburseId) {
        return sysReimburseMapper.submitReimburse(reimburseId);
    }

    @Override
    public List<TravelStatistic> getTravelStatistics(String startMonth, String endMonth) {
        List<SysReimburse> reimList = sysReimburseMapper.getTravelStatistics(startMonth, endMonth);
        if (reimList == null || reimList.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, List<SysReimburse>> groupByCreateBy = reimList.stream()
                .filter(reimburse -> reimburse.getCreateBy() != null) // 过滤createBy为空的记录
                .collect(Collectors.groupingBy(SysReimburse::getCreateBy));

        // 2. 遍历分组，组装TravelStatistic
        List<TravelStatistic> travelStatisticList = new ArrayList<>();
        for (Map.Entry<String, List<SysReimburse>> entry : groupByCreateBy.entrySet()) {
            String userName = entry.getKey();
            List<SysReimburse> groupList = entry.getValue();

            // 2.1 转换为TravelPeriod列表
            List<TravelPeriod> travelPeriodList = groupList.stream()
                    .map(this::convertToTravelPeriod)
                    .collect(Collectors.toList());

            // 2.2 创建TravelStatistic并赋值
            TravelStatistic travelStatistic = new TravelStatistic();
            travelStatistic.setCreateBy(userName);
            travelStatistic.setTravelPeriodList(travelPeriodList);
            // 若需要设置createBy，可从分组内的SysReimburse取（此处取第一个非空值）
            Optional<String> nickName = groupList.stream()
                    .map(SysReimburse::getNickName)
                    .filter(Objects::nonNull)
                    .findFirst();
            nickName.ifPresent(travelStatistic::setNickName);

            travelStatisticList.add(travelStatistic);
        }
        return travelStatisticList;
    }

    private TravelPeriod convertToTravelPeriod(SysReimburse reimburse) {
        TravelPeriod travelPeriod = new TravelPeriod();

        // 日期转换：Date -> String（处理null值）
        if (reimburse.getStartTime() != null) {
            travelPeriod.setStartTime(DATE_FORMATTER.format(reimburse.getStartTime()));
        }
        if (reimburse.getEndTime() != null) {
            travelPeriod.setEndTime(DATE_FORMATTER.format(reimburse.getEndTime()));
        }

        // 金额直接赋值
        travelPeriod.setTotalAmount(reimburse.getTotalAmount());

        return travelPeriod;
    }

    @Override
    public boolean checkTimePeriod(String startDate, String endDate, String userName,Long reimburseId) {
        List<SysReimburse> list = sysReimburseMapper.checkTimePeriod(startDate, endDate, userName, reimburseId);
        if(list.size()>0){
            return false;
        }
        return true;
    }
}
