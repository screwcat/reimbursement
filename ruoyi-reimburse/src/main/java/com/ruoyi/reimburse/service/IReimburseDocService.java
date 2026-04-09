package com.ruoyi.reimburse.service;

import java.util.List;

import com.ruoyi.reimburse.domain.ReimburseDoc;

/**
 * 报销单据主Service接口
 *
 * @author ruoyi
 * @date 2026-04-03
 */
public interface IReimburseDocService
{
    /**
     * 查询报销单据主
     *
     * @param docId 报销单据主主键
     * @return 报销单据主
     */
    public ReimburseDoc selectReimburseDocByDocId(Long docId);

    public ReimburseDoc selectReimburseSummaryByDocId(Long docId);

    /**
     * 查询报销单据主列表
     *
     * @param ReimburseDoc 报销单据主
     * @return 报销单据主集合
     */
    public List<ReimburseDoc> selectReimburseDocList(ReimburseDoc ReimburseDoc);

    public List<ReimburseDoc> selectReimburseDoclistSummary(ReimburseDoc ReimburseDoc);


    /**
     * 新增报销单据主
     *
     * @param ReimburseDoc 报销单据主
     * @return 结果
     */
    public int insertReimburseDoc(ReimburseDoc ReimburseDoc);

    /**
     * 修改报销单据主
     *
     * @param ReimburseDoc 报销单据主
     * @return 结果
     */
    public int updateReimburseDoc(ReimburseDoc ReimburseDoc);

    /**
     * 批量删除报销单据主
     *
     * @param docIds 需要删除的报销单据主主键集合
     * @return 结果
     */
    public int deleteReimburseDocByDocIds(Long[] docIds);

    /**
     * 删除报销单据主信息
     *
     * @param docId 报销单据主主键
     * @return 结果
     */
    public int deleteReimburseDocByDocId(Long docId);

    public int submitReimburse(Long docId);

    public int changeProcessState(Long docId, String processState);
}
