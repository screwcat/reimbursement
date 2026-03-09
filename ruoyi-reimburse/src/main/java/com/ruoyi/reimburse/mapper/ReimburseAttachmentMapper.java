package com.ruoyi.reimburse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.reimburse.domain.ReimburseAttachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报销票据影像附件Mapper接口
 *
 * @author ruoyi
 */
public interface ReimburseAttachmentMapper extends BaseMapper<ReimburseAttachment>
{
    /**
     * 根据报销单ID查询附件列表
     *
     * @param reimburseId 报销单ID
     * @return 附件列表
     */
    List<ReimburseAttachment> selectReimburseAttachmentByReimburseId(@Param("reimburseId") Long reimburseId);

    /**
     * 根据报销单ID删除附件
     *
     * @param reimburseId 报销单ID
     * @return 结果
     */
    int deleteReimburseAttachmentByReimburseId(@Param("reimburseId") Long reimburseId);
}
