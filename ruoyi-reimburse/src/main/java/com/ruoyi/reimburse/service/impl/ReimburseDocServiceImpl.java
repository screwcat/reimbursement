package com.ruoyi.reimburse.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.reimburse.domain.ReimburseDoc;
import com.ruoyi.reimburse.mapper.ReimburseDocMapper;
import com.ruoyi.reimburse.service.IReimburseDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 报销单据主Service业务层处理
 *
 * @author ruoyi
 * @date 2026-04-03
 */
@Service
public class ReimburseDocServiceImpl implements IReimburseDocService
{
    @Autowired
    private ReimburseDocMapper reimburseDocMapper;

    /**
     * 查询报销单据主
     *
     * @param docId 报销单据主主键
     * @return 报销单据主
     */
    @Override
    public ReimburseDoc selectReimburseDocByDocId(Long docId)
    {
        return reimburseDocMapper.selectReimburseDocByDocId(docId);
    }

    @Override
    public ReimburseDoc selectReimburseSummaryByDocId(Long docId) {
        ReimburseDoc reimburseDoc = new ReimburseDoc();
        reimburseDoc.setDocId(docId);
        return reimburseDocMapper.selectReimburseDoclistSummary(reimburseDoc).get(0);
    }


    /**
     * 查询报销单据主列表
     *
     * @param ReimburseDoc 报销单据主
     * @return 报销单据主
     */
    @Override
    public List<ReimburseDoc> selectReimburseDocList(ReimburseDoc ReimburseDoc)
    {
        return reimburseDocMapper.selectReimburseDocList(ReimburseDoc);
    }

    @Override
    public List<ReimburseDoc> selectReimburseDoclistSummary(ReimburseDoc ReimburseDoc) {
        if(!isAdmin()&&!hasRole("finance")){
            ReimburseDoc.setUserName(getUsername());
        }
        return reimburseDocMapper.selectReimburseDoclistSummary(ReimburseDoc);
    }

    /**
     * 新增报销单据主
     *
     * @param ReimburseDoc 报销单据主
     * @return 结果
     */
    @Override
    public int insertReimburseDoc(ReimburseDoc ReimburseDoc)
    {
        ReimburseDoc.setCreateTime(DateUtils.getNowDate());
        ReimburseDoc.setUserName(getUsername());
        ReimburseDoc.setProcessStatus("DRAFT");
        return reimburseDocMapper.insertReimburseDoc(ReimburseDoc);
    }

    /**
     * 修改报销单据主
     *
     * @param ReimburseDoc 报销单据主
     * @return 结果
     */
    @Override
    public int updateReimburseDoc(ReimburseDoc ReimburseDoc)
    {
        return reimburseDocMapper.updateReimburseDoc(ReimburseDoc);
    }

    /**
     * 批量删除报销单据主
     *
     * @param docIds 需要删除的报销单据主主键
     * @return 结果
     */
    @Override
    public int deleteReimburseDocByDocIds(Long[] docIds)
    {
        return reimburseDocMapper.deleteReimburseDocByDocIds(docIds);
    }

    /**
     * 删除报销单据主信息
     *
     * @param docId 报销单据主主键
     * @return 结果
     */
    @Override
    public int deleteReimburseDocByDocId(Long docId)
    {
        return reimburseDocMapper.deleteReimburseDocByDocId(docId);
    }

    @Override
    public int submitReimburse(Long docId) {
        return reimburseDocMapper.submitReimburse(docId);
    }

    @Override
    public int changeProcessState(Long docId, String processState) {
        return reimburseDocMapper.changeProcessState(docId,processState);
    }

}
