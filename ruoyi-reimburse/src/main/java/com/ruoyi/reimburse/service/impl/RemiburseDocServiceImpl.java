package com.ruoyi.reimburse.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.reimburse.domain.RemiburseDoc;
import com.ruoyi.reimburse.mapper.RemiburseDocMapper;
import com.ruoyi.reimburse.service.IRemiburseDocService;
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
public class RemiburseDocServiceImpl implements IRemiburseDocService
{
    @Autowired
    private RemiburseDocMapper remiburseDocMapper;

    /**
     * 查询报销单据主
     *
     * @param docId 报销单据主主键
     * @return 报销单据主
     */
    @Override
    public RemiburseDoc selectRemiburseDocByDocId(Long docId)
    {
        return remiburseDocMapper.selectRemiburseDocByDocId(docId);
    }

    @Override
    public RemiburseDoc selectRemiburseSummaryByDocId(Long docId) {
        RemiburseDoc remiburseDoc = new RemiburseDoc();
        remiburseDoc.setDocId(docId);
        return remiburseDocMapper.selectRemiburseDoclistSummary(remiburseDoc).get(0);
    }


    /**
     * 查询报销单据主列表
     *
     * @param remiburseDoc 报销单据主
     * @return 报销单据主
     */
    @Override
    public List<RemiburseDoc> selectRemiburseDocList(RemiburseDoc remiburseDoc)
    {
        return remiburseDocMapper.selectRemiburseDocList(remiburseDoc);
    }

    @Override
    public List<RemiburseDoc> selectRemiburseDoclistSummary(RemiburseDoc remiburseDoc) {
        if(!isAdmin()&&!hasRole("finance")){
            remiburseDoc.setUserName(getUsername());
        }
        return remiburseDocMapper.selectRemiburseDoclistSummary(remiburseDoc);
    }

    /**
     * 新增报销单据主
     *
     * @param remiburseDoc 报销单据主
     * @return 结果
     */
    @Override
    public int insertRemiburseDoc(RemiburseDoc remiburseDoc)
    {
        remiburseDoc.setCreateTime(DateUtils.getNowDate());
        remiburseDoc.setUserName(getUsername());
        remiburseDoc.setProcessStatus("DRAFT");
        return remiburseDocMapper.insertRemiburseDoc(remiburseDoc);
    }

    /**
     * 修改报销单据主
     *
     * @param remiburseDoc 报销单据主
     * @return 结果
     */
    @Override
    public int updateRemiburseDoc(RemiburseDoc remiburseDoc)
    {
        return remiburseDocMapper.updateRemiburseDoc(remiburseDoc);
    }

    /**
     * 批量删除报销单据主
     *
     * @param docIds 需要删除的报销单据主主键
     * @return 结果
     */
    @Override
    public int deleteRemiburseDocByDocIds(Long[] docIds)
    {
        return remiburseDocMapper.deleteRemiburseDocByDocIds(docIds);
    }

    /**
     * 删除报销单据主信息
     *
     * @param docId 报销单据主主键
     * @return 结果
     */
    @Override
    public int deleteRemiburseDocByDocId(Long docId)
    {
        return remiburseDocMapper.deleteRemiburseDocByDocId(docId);
    }

    @Override
    public int submitReimburse(Long docId) {
        return remiburseDocMapper.submitReimburse(docId);
    }

    @Override
    public int changeProcessState(Long docId, String processState) {
        return remiburseDocMapper.changeProcessState(docId,processState);
    }

}
