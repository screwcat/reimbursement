package com.reimburse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reimburse.domain.Reimburse;
import com.reimburse.domain.ReimburseAttachment;
import com.reimburse.domain.ReimburseDetail;

import java.util.List;
import java.util.Map;

/**
 * 报销申请Service接口
 *
 * @author ruoyi
 */
public interface IReimburseService extends IService<Reimburse>
{
    /**
     * 查询报销申请列表
     *
     * @param reimburse 报销申请
     * @return 报销申请集合
     */
    List<Reimburse> selectReimburseList(Reimburse reimburse);

    /**
     * 新增报销申请（含明细+附件）
     *
     * @param reimburse 报销申请
     * @param detailList 明细列表
     * @param attachmentList 附件列表
     * @return 结果
     */
    int insertReimburse(Reimburse reimburse, List<ReimburseDetail> detailList, List<ReimburseAttachment> attachmentList);

    /**
     * 修改报销申请（含明细+附件）
     *
     * @param reimburse 报销申请
     * @param detailList 明细列表
     * @param attachmentList 附件列表
     * @return 结果
     */
    int updateReimburse(Reimburse reimburse, List<ReimburseDetail> detailList, List<ReimburseAttachment> attachmentList);

    /**
     * 删除报销申请（含明细+附件）
     *
     * @param reimburseIds 报销单ID数组
     * @return 结果
     */
    int deleteReimburseByIds(Long[] reimburseIds);

    /**
     * 提交审批
     *
     * @param reimburseId 报销单ID
     * @return 结果
     */
    int submitApproval(Long reimburseId);

    /**
     * 撤销申请
     *
     * @param reimburseId 报销单ID
     * @return 结果
     */
    int cancelApplication(Long reimburseId);

    /**
     * 查询报销申请详情（含明细+附件）
     *
     * @param reimburseId 报销单ID
     * @return Map(key: reimburse, detailList, attachmentList)
     */
    Map<String, Object> selectReimburseDetail(Long reimburseId);

    /**
     * 生成唯一单据编号
     *
     * @return 单据编号
     */
    String generateBillNo();
}
