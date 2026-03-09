package com.ruoyi.reimburse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.reimburse.domain.Reimburse;

import java.util.List;

/**
 * 报销申请Mapper接口
 *
 * @author ruoyi
 */
public interface ReimburseMapper extends BaseMapper<Reimburse>
{
    /**
     * 查询报销申请列表
     *
     * @param reimburse 报销申请
     * @return 报销申请集合
     */
    List<Reimburse> selectReimburseList(Reimburse reimburse);

    /**
     * 根据单据编号查询报销单
     *
     * @param billNo 单据编号
     * @return 报销申请
     */
//    Reimburse selectReimburseByBillNo(@Param("billNo") String billNo);
}
