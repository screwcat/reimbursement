package com.reimburse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reimburse.domain.ReimburseDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报销费用明细Mapper接口
 *
 * @author ruoyi
 */
public interface ReimburseDetailMapper extends BaseMapper<ReimburseDetail>
{
    /**
     * 根据报销单ID查询明细列表
     *
     * @param reimburseId 报销单ID
     * @return 明细列表
     */
    List<ReimburseDetail> selectReimburseDetailByReimburseId(@Param("reimburseId") Long reimburseId);

    /**
     * 根据报销单ID删除明细
     *
     * @param reimburseId 报销单ID
     * @return 结果
     */
    int deleteReimburseDetailByReimburseId(@Param("reimburseId") Long reimburseId);
}
