package com.ruoyi.reimburse.service;

import java.util.List;

import com.ruoyi.reimburse.domain.RemiburseDoc;

/**
 * 报销单据主Service接口
 *
 * @author ruoyi
 * @date 2026-04-03
 */
public interface IRemiburseDocService
{
    /**
     * 查询报销单据主
     *
     * @param docId 报销单据主主键
     * @return 报销单据主
     */
    public RemiburseDoc selectRemiburseDocByDocId(Long docId);

    /**
     * 查询报销单据主列表
     *
     * @param remiburseDoc 报销单据主
     * @return 报销单据主集合
     */
    public List<RemiburseDoc> selectRemiburseDocList(RemiburseDoc remiburseDoc);

    /**
     * 新增报销单据主
     *
     * @param remiburseDoc 报销单据主
     * @return 结果
     */
    public int insertRemiburseDoc(RemiburseDoc remiburseDoc);

    /**
     * 修改报销单据主
     *
     * @param remiburseDoc 报销单据主
     * @return 结果
     */
    public int updateRemiburseDoc(RemiburseDoc remiburseDoc);

    /**
     * 批量删除报销单据主
     *
     * @param docIds 需要删除的报销单据主主键集合
     * @return 结果
     */
    public int deleteRemiburseDocByDocIds(Long[] docIds);

    /**
     * 删除报销单据主信息
     *
     * @param docId 报销单据主主键
     * @return 结果
     */
    public int deleteRemiburseDocByDocId(Long docId);
}
