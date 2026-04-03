package com.ruoyi.reimburse.mapper;

import com.ruoyi.reimburse.domain.RemiburseDoc;

import java.util.List;

/**
 * 报销单据主Mapper接口
 *
 * @author ruoyi
 * @date 2026-04-03
 */
public interface RemiburseDocMapper
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
     * 删除报销单据主
     *
     * @param docId 报销单据主主键
     * @return 结果
     */
    public int deleteRemiburseDocByDocId(Long docId);

    /**
     * 批量删除报销单据主
     *
     * @param docIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRemiburseDocByDocIds(Long[] docIds);
}
